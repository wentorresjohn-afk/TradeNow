const API_URL = 'https://tradenow-437n.onrender.com/api';

const profileContainer = document.getElementById('profile-card-container');
const myPostsContainer = document.getElementById('my-posts-container');

document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');

    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

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

    loadProfile(userId);
    loadMyPosts(userId);
});

async function loadProfile(userId) {
    try {
        const response = await fetch(`${API_URL}/user/${userId}`);

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar el perfil`);
        }

        const user = await response.json();
        renderProfile(user);

    } catch (error) {
        console.error('Error cargando perfil:', error);
        profileContainer.innerHTML = '<p class="empty-state">No se pudo cargar tu perfil.</p>';
    }
}

function renderProfile(user) {
    const rating = user.averageRating != null ? user.averageRating.toFixed(1) : '0.0';

    profileContainer.innerHTML = `
        <div class="profile-card">
            <img class="profile-photo" src="${user.photo || 'https://via.placeholder.com/100'}" alt="Foto de perfil">
            <div class="profile-info">
                <h2>${user.name || 'Sin nombre'}</h2>
                <div class="email">${user.email || ''}</div>
                <div>
                    <span class="badge type">${user.userType || 'GENERAL'}</span>
                    <span class="badge ${user.active ? 'active' : 'inactive'}">${user.active ? 'Activo' : 'Inactivo'}</span>
                </div>
                <p class="profile-description">${user.description || 'Sin descripción aún.'}</p>
                <p style="font-size: 0.85rem; color: #6b7280; margin-top: 8px;">📍 ${user.geographicZone || 'Zona no especificada'}</p>
                <button class="btn-edit" onclick="window.location.href='editar-perfil.html'">Editar Perfil</button>
            </div>
        </div>

        <div class="stats-row">
            <div class="stat-box">
                <div class="value">⭐ ${rating}</div>
                <div class="label">Calificación promedio</div>
            </div>
            <div class="stat-box">
                <div class="value">${user.completedTrades ?? 0}</div>
                <div class="label">Trueques completados</div>
            </div>
        </div>
    `;
}

async function loadMyPosts(userId) {
    try {
        const response = await fetch(`${API_URL}/publicaciones/mias?userId=${userId}`);

        if (response.status === 204) {
            myPostsContainer.innerHTML = '<p class="empty-state">Aún no has creado publicaciones.</p>';
            return;
        }

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar publicaciones`);
        }

        const myPosts = await response.json();
        renderMyPosts(myPosts);

    } catch (error) {
        console.error('Error cargando mis publicaciones:', error);
        myPostsContainer.innerHTML = '<p class="empty-state">Ocurrió un error al cargar tus publicaciones.</p>';
    }
}

function renderMyPosts(posts) {
    myPostsContainer.innerHTML = '';

    if (!posts || posts.length === 0) {
        myPostsContainer.innerHTML = '<p class="empty-state">Aún no has creado publicaciones.</p>';
        return;
    }

    const statusColors = {
        PENDING: '#f59e0b',
        APPROVED: '#10b981',
        REJECTED: '#ef4444',
        HIDDEN: '#6b7280'
    };

    posts.forEach(post => {
        const card = document.createElement('div');
        card.className = 'post-card';

        card.innerHTML = `
            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="${post.title || 'Publicación'}">` : ''}
            <div>
                <span class="post-tag">${post.type || 'Trueque'}</span>
                <h4>${post.title || 'Sin título'}</h4>
                <p>${post.description || 'Sin descripción'}</p>
                <p class="post-status" style="color: ${statusColors[post.status] || '#6b7280'};">
                    ${post.status || 'PENDING'}
                </p>
            </div>
        `;
        myPostsContainer.appendChild(card);
    });
}