const API_URL = 'https://tradenow-437n.onrender.com/api';

let currentAdminId = null;

document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');
    const userType = localStorage.getItem('userType');

    // Seguridad: si no hay sesión, al login
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    // Seguridad: solo ADMINISTRATOR puede entrar a esta página
    if (userType !== 'ADMINISTRATOR') {
        window.location.href = 'dashboard.html';
        return;
    }

    currentAdminId = parseInt(userId);

    const userDisplay = document.getElementById('user-display-name');
    if (userDisplay) {
        userDisplay.textContent = `Usuario: ${userName}`;
    }

    const logoutBtn = document.getElementById('btn-session-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = 'login.html';
        });
    }

    loadUsers();
    loadPosts();
});

const usersContainer = document.getElementById('users-container');
const postsContainer = document.getElementById('posts-container');

// ==========================================
// Cargar y mostrar usuarios
// ==========================================
async function loadUsers() {
    usersContainer.innerHTML = '<p class="loading-state">Cargando usuarios...</p>';

    try {
        const response = await fetch(`${API_URL}/users`);

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar usuarios`);
        }

        const users = await response.json();
        renderUsers(users);

    } catch (error) {
        console.error('Error cargando usuarios:', error);
        usersContainer.innerHTML = '<p class="empty-state">Ocurrió un error al cargar los usuarios.</p>';
    }
}

function renderUsers(users) {
    if (!users || users.length === 0) {
        usersContainer.innerHTML = '<p class="empty-state">No hay usuarios registrados.</p>';
        return;
    }

    let rows = '';
    users.forEach(user => {
        // No mostramos botón de eliminar para el propio admin que tiene la sesión abierta
        const isSelf = user.id === currentAdminId;

        rows += `
            <tr id="user-row-${user.id}">
                <td>${user.id}</td>
                <td>${user.name || 'Sin nombre'}</td>
                <td>${user.email || ''}</td>
                <td><span class="badge ${user.userType}">${user.userType}</span></td>
                <td>
                    ${isSelf
                        ? '<span style="color:#9ca3af; font-size:0.8rem;">Tu cuenta</span>'
                        : `<button class="btn-action btn-danger" onclick="deleteUser(${user.id})">Eliminar</button>`
                    }
                </td>
            </tr>
        `;
    });

    usersContainer.innerHTML = `
        <table class="users-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

async function deleteUser(userId) {
    const confirmDelete = confirm('¿Seguro que deseas eliminar este usuario? Esta acción no se puede deshacer.');
    if (!confirmDelete) return;

    try {
        const response = await fetch(`${API_URL}/user/${userId}?adminId=${currentAdminId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || 'Error al eliminar el usuario');
        }

        const row = document.getElementById(`user-row-${userId}`);
        if (row) row.remove();

    } catch (error) {
        alert('Error: ' + error.message);
        console.error('Detalle:', error);
    }
}

// ==========================================
// Cargar y mostrar publicaciones
// ==========================================
async function loadPosts() {
    postsContainer.innerHTML = '<p class="loading-state">Cargando publicaciones...</p>';

    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);

        if (response.status === 204) {
            postsContainer.innerHTML = '<p class="empty-state">No hay publicaciones disponibles.</p>';
            return;
        }

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar publicaciones`);
        }

        const posts = await response.json();
        renderPosts(posts);

    } catch (error) {
        console.error('Error cargando publicaciones:', error);
        postsContainer.innerHTML = '<p class="empty-state">Ocurrió un error al cargar las publicaciones.</p>';
    }
}

function renderPosts(posts) {
    postsContainer.innerHTML = '';

    if (!posts || posts.length === 0) {
        postsContainer.innerHTML = '<p class="empty-state">No hay publicaciones disponibles.</p>';
        return;
    }

    posts.forEach(post => {
        const card = document.createElement('div');
        card.className = 'post-card';
        card.id = `post-card-${post.id}`;

        card.innerHTML = `
            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="${post.title || 'Publicación'}">` : ''}
            <div>
                <span class="post-tag">${post.type || 'Trueque'}</span>
                <h4>${post.title || 'Sin título'}</h4>
                <p>${post.description || 'Sin descripción'}</p>
                <div class="post-meta">
                    Autor: ${post.user && post.user.name ? post.user.name : 'Desconocido'} · Estado: ${post.status || 'N/A'}
                </div>
            </div>
            <button class="btn-action btn-danger" onclick="deletePost(${post.id})">Eliminar</button>
        `;
        postsContainer.appendChild(card);
    });
}

async function deletePost(postId) {
    const confirmDelete = confirm('¿Seguro que deseas eliminar esta publicación? Esta acción no se puede deshacer.');
    if (!confirmDelete) return;

    try {
        const response = await fetch(`${API_URL}/publicaciones/delete/${postId}?adminId=${currentAdminId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || 'Error al eliminar la publicación');
        }

        const card = document.getElementById(`post-card-${postId}`);
        if (card) card.remove();

    } catch (error) {
        alert('Error: ' + error.message);
        console.error('Detalle:', error);
    }
}