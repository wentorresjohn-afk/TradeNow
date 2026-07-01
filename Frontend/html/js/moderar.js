const API_URL = 'https://tradenow-437n.onrender.com/api';

let currentModeratorId = null;

document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');
    const userType = localStorage.getItem('userType');

    // Seguridad: si no hay sesión, al login
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    // Seguridad: si no es MODERATOR ni ADMINISTRATOR, lo mandamos de vuelta al dashboard
    if (userType !== 'MODERATOR' && userType !== 'ADMINISTRATOR') {
        window.location.href = 'dashboard.html';
        return;
    }

    currentModeratorId = parseInt(userId);

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

    loadPendingPosts();
});

const pendingContainer = document.getElementById('pending-posts-container');

async function loadPendingPosts() {
    pendingContainer.innerHTML = '<p class="loading-state">Cargando publicaciones pendientes...</p>';

    try {
        const response = await fetch(`${API_URL}/publicaciones/pendientes?moderatorId=${currentModeratorId}`);

        if (response.status === 204) {
            pendingContainer.innerHTML = '<p class="empty-state">No hay publicaciones pendientes de moderación.</p>';
            return;
        }

        if (response.status === 403) {
            pendingContainer.innerHTML = '<p class="empty-state">No tienes permisos para ver esta sección.</p>';
            return;
        }

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar publicaciones pendientes`);
        }

        const posts = await response.json();
        renderPendingPosts(posts);

    } catch (error) {
        console.error('Error cargando publicaciones pendientes:', error);
        pendingContainer.innerHTML = '<p class="empty-state">Ocurrió un error al cargar las publicaciones.</p>';
    }
}

function renderPendingPosts(posts) {
    pendingContainer.innerHTML = '';

    if (!posts || posts.length === 0) {
        pendingContainer.innerHTML = '<p class="empty-state">No hay publicaciones pendientes de moderación.</p>';
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
                    Autor: ${post.user && post.user.name ? post.user.name : 'Desconocido'} · ID: ${post.id}
                </div>
            </div>
            <div class="post-footer">
                <button class="btn-action btn-approve" onclick="moderatePost(${post.id}, 'APPROVED')">Aprobar</button>
                <button class="btn-action btn-reject" onclick="moderatePost(${post.id}, 'REJECTED')">Rechazar</button>
            </div>
        `;
        pendingContainer.appendChild(card);
    });
}

async function moderatePost(postId, status) {
    try {
        const response = await fetch(`${API_URL}/publicaciones/${postId}/moderar`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                status: status,
                moderatorId: currentModeratorId
            })
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || 'Error al moderar la publicación');
        }

        // Quitamos la tarjeta de la vista una vez moderada
        const card = document.getElementById(`post-card-${postId}`);
        if (card) card.remove();

        if (pendingContainer.children.length === 0) {
            pendingContainer.innerHTML = '<p class="empty-state">No hay publicaciones pendientes de moderación.</p>';
        }

    } catch (error) {
        alert('Error: ' + error.message);
        console.error('Detalle:', error);
    }
}