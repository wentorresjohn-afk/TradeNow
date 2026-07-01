const API_URL = 'https://tradenow-437n.onrender.com/api';

document.addEventListener('DOMContentLoaded', () => {
    // 1. Obtener los datos guardados en localStorage
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');
    const userType = localStorage.getItem('userType');

    // 2. Seguridad: Si no hay ID, redirigir al login
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    // 3. Pintar nombre del usuario
    const userDisplay = document.getElementById('user-display-name');
    if (userDisplay) {
        userDisplay.textContent = `Usuario: ${userName}`;
    }

    // 3.1 Pintar saludo de bienvenida
    const welcomeName = document.getElementById('welcome-user-name');
    if (welcomeName) {
        welcomeName.textContent = `¡Bienvenido de nuevo, ${userName}!`;
    }

    // 3.2 Gestión de visibilidad del menú según rol
    const navCreatePost = document.getElementById('nav-create-post');
    const navModerar = document.getElementById('nav-moderar');
    const navAdmin = document.getElementById('nav-admin');

    // Si es Administrador, ocultamos el botón de crear y mostramos las herramientas de admin
    if (userType === 'ADMINISTRATOR') {
        if (navCreatePost) navCreatePost.classList.add('hidden');
        if (navModerar) navModerar.classList.remove('hidden');
        if (navAdmin) navAdmin.classList.remove('hidden');
    } 
    // Si es moderador, solo mostramos el botón de moderación
    else if (userType === 'MODERATOR') {
        if (navModerar) navModerar.classList.remove('hidden');
    }

    // 4. Botón de Cerrar Sesión
    const logoutBtn = document.getElementById('btn-session-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = 'login.html';
        });
    }

    // 5. Cargar publicaciones
    loadPosts();
});

const postsContainer = document.getElementById('posts-container');

async function loadPosts() {
    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);

        if (response.status === 204) {
            postsContainer.innerHTML = '<p class="empty-state">Aún no hay publicaciones disponibles.</p>';
            return;
        }

        if (!response.ok) throw new Error(`Error ${response.status}`);

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
            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="${post.title}">` : ''}
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

// ==========================================
// Lógica de Propuestas
// ==========================================
const proposalModal = document.getElementById('proposal-modal');
const formCreateProposal = document.getElementById('form-create-proposal');
const offeredPostSelect = document.getElementById('proposal-offered-post');
const btnSubmitProposal = document.getElementById('btn-submit-proposal');

async function openProposalModal(targetPostId, postTitle) {
    document.getElementById('proposal-target-id').value = targetPostId;
    document.getElementById('proposal-modal-text').textContent = `Vas a proponer un trueque por: "${postTitle}"`;
    proposalModal.classList.remove('hidden');
    await loadMyPostsForSelect(targetPostId);
}

async function loadMyPostsForSelect(excludePostId) {
    offeredPostSelect.innerHTML = '<option value="" disabled selected>Cargando tus publicaciones...</option>';
    const userId = localStorage.getItem('userId');

    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);
        if (response.status === 204) { showNoPostsState(); return; }
        
        const allPosts = await response.json();
        const myPosts = allPosts.filter(p => p.user && p.user.id == userId && p.id != excludePostId);

        if (myPosts.length === 0) { showNoPostsState(); return; }

        offeredPostSelect.innerHTML = '<option value="" disabled selected>Selecciona una publicación</option>';
        myPosts.forEach(p => {
            offeredPostSelect.innerHTML += `<option value="${p.id}">${p.title}</option>`;
        });
    } catch (error) {
        offeredPostSelect.innerHTML = '<option value="" disabled selected>Error al cargar</option>';
    }
}

function showNoPostsState() {
    offeredPostSelect.innerHTML = '<option value="" disabled selected>No tienes publicaciones disponibles</option>';
    btnSubmitProposal.disabled = true;
}

document.getElementById('btn-close-proposal').addEventListener('click', () => {
    proposalModal.classList.add('hidden');
});

formCreateProposal.addEventListener('submit', async (e) => {
    e.preventDefault();
    const userId = localStorage.getItem('userId');
    const offeredPublicationId = offeredPostSelect.value;

    const proposalData = {
        senderId: parseInt(userId),
        targetPublicationId: parseInt(document.getElementById('proposal-target-id').value),
        offeredPublicationId: parseInt(offeredPublicationId)
    };

    try {
        const response = await fetch(`${API_URL}/propuestas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(proposalData)
        });

        if (!response.ok) throw new Error('Error al enviar la propuesta');

        alert('¡Propuesta enviada con éxito!');
        proposalModal.classList.add('hidden');
    } catch (error) {
        alert('Error: ' + error.message);
    }
});