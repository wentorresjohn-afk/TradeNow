const API_URL = 'https://tradenow-437n.onrender.com/api';

document.addEventListener('DOMContentLoaded', () => {
    // 1. Obtener los datos que guardaste en el localStorage al iniciar sesión
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');

    // 2. Seguridad: Si no hay un ID de usuario, redirigir inmediatamente al login
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    // 3. Pintar el nombre del usuario en la barra superior
    const userDisplay = document.getElementById('user-display-name');
    if (userDisplay) {
        userDisplay.textContent = `Usuario: ${userName}`;
    }

    // 4. Lógica para el botón de Cerrar Sesión
    const logoutBtn = document.getElementById('btn-session-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = 'login.html';
        });
    }

    // 5. Cargar las publicaciones
    loadPosts();
});

const postsContainer = document.getElementById('posts-container');

async function loadPosts() {
    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);

        // 204 No Content -> no hay publicaciones
        if (response.status === 204) {
            postsContainer.innerHTML = '<p class="empty-state">Aún no hay publicaciones disponibles.</p>';
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
        postsContainer.innerHTML = '<p class="empty-state">Aún no hay publicaciones disponibles.</p>';
        return;
    }

    posts.forEach(post => {
        const card = document.createElement('div');
        card.className = 'post-card';

        card.innerHTML = `
            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="${post.title || 'Publicación'}">` : ''}
            <div>
                <span class="post-tag">${post.type || 'Trueque'}</span>
                <h4>${post.title || 'Sin título'}</h4>
                <p>${post.description || 'Sin descripción'}</p>
            </div>
            <div class="post-footer">
                <span style="font-size: 0.8rem; color: #94a3b8;">ID: ${post.id}</span>
                <button onclick="openProposalModal(${post.id}, '${post.title}')" class="btn-action btn-primary" style="padding: 6px 12px; font-size: 0.8rem;">
                    Ofrecer Trueque
                </button>
            </div>
        `;
        postsContainer.appendChild(card);
    });
}