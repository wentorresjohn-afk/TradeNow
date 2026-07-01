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

     // 3.1 Mostrar el link de moderación solo si el usuario es MODERATOR o ADMINISTRATOR
    const userType = localStorage.getItem('userType');
    const navModerar = document.getElementById('nav-moderar');
    if (navModerar && (userType === 'MODERATOR' || userType === 'ADMINISTRATOR')) {
    navModerar.classList.remove('hidden');
    }

    // 3.2 Mostrar el link de administrar usuarios solo si el usuario es ADMINISTRATOR
    const navAdmin = document.getElementById('nav-admin');
    if (navAdmin && userType === 'ADMINISTRATOR') {
    navAdmin.classList.remove('hidden');
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

// ==========================================
// RF9: Enviar propuesta de intercambio
// ==========================================
const proposalModal = document.getElementById('proposal-modal');
const formCreateProposal = document.getElementById('form-create-proposal');
const offeredPostSelect = document.getElementById('proposal-offered-post');
const noPostsMsg = document.getElementById('proposal-no-posts-msg');
const btnSubmitProposal = document.getElementById('btn-submit-proposal');

async function openProposalModal(targetPostId, postTitle) {
    document.getElementById('proposal-target-id').value = targetPostId;
    document.getElementById('proposal-modal-text').textContent = `Vas a proponer un trueque por: "${postTitle}"`;
    proposalModal.classList.remove('hidden');
    await loadMyPostsForSelect(targetPostId);
}

// Carga las publicaciones del usuario actual para elegir cuál ofrece
async function loadMyPostsForSelect(excludePostId) {
    offeredPostSelect.innerHTML = '<option value="" disabled selected>Cargando tus publicaciones...</option>';
    noPostsMsg.classList.add('hidden');
    btnSubmitProposal.disabled = false;

    const userId = localStorage.getItem('userId');

    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);
        if (response.status === 204) {
            showNoPostsState();
            return;
        }
        if (!response.ok) throw new Error('Error al cargar tus publicaciones');

        const allPosts = await response.json();
        const myPosts = allPosts.filter(p =>
            p.user && p.user.id == userId && p.id != excludePostId
        );

        if (myPosts.length === 0) {
            showNoPostsState();
            return;
        }

        offeredPostSelect.innerHTML = '<option value="" disabled selected>Selecciona una publicación</option>';
        myPosts.forEach(p => {
            offeredPostSelect.innerHTML += `<option value="${p.id}">${p.title}</option>`;
        });

    } catch (error) {
        console.error('Error cargando publicaciones propias:', error);
        offeredPostSelect.innerHTML = '<option value="" disabled selected>Error al cargar</option>';
    }
}

function showNoPostsState() {
    offeredPostSelect.innerHTML = '<option value="" disabled selected>No tienes publicaciones</option>';
    noPostsMsg.classList.remove('hidden');
    btnSubmitProposal.disabled = true;
}

document.getElementById('btn-close-proposal').addEventListener('click', () => {
    proposalModal.classList.add('hidden');
});

formCreateProposal.addEventListener('submit', async (e) => {
    e.preventDefault();

    const userId = localStorage.getItem('userId');
    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    const offeredPublicationId = offeredPostSelect.value;
    if (!offeredPublicationId) {
        alert('Selecciona una publicación para ofrecer.');
        return;
    }

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

        if (!response.ok) {
            const data = await response.json().catch(() => null);
            const errorMsg = Array.isArray(data) ? data.join(', ') : (data || 'Error al enviar la propuesta');
            throw new Error(errorMsg);
        }

        alert('¡Propuesta enviada con éxito!');
        proposalModal.classList.add('hidden');

    } catch (error) {
        alert('Error: ' + error.message);
        console.error('Detalle:', error);
    }
});