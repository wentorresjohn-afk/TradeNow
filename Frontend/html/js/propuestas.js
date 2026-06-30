const API_URL = 'https://tradenow-437n.onrender.com/api';

const currentUserId = localStorage.getItem('userId');
if (!currentUserId) window.location.href = 'login.html';

const statusLabels = {
    PENDING: 'Pendiente',
    ACCEPTED: 'Aceptada',
    REJECTED: 'Rechazada',
    COUNTERED: 'Contraoferta enviada'
};

document.addEventListener('DOMContentLoaded', () => {
    loadSentProposals();
    loadReceivedProposals();
});

// ==========================================
// RF11: Historial de propuestas enviadas y recibidas
// ==========================================
async function loadSentProposals() {
    const res = await fetch(`${API_URL}/propuestas/enviadas?userId=${currentUserId}`);
    if (res.ok) {
        const data = await res.json();
        renderProposals(data, 'enviadas-container', false);
    }
}

async function loadReceivedProposals() {
    const res = await fetch(`${API_URL}/propuestas/recibidas?userId=${currentUserId}`);
    if (res.ok) {
        const data = await res.json();
        renderProposals(data, 'recibidas-container', true);
    }
}

function renderProposals(proposals, containerId, isReceived) {
    const container = document.getElementById(containerId);
    container.innerHTML = proposals.length === 0
        ? '<p style="color: #94a3b8; font-size: 0.9rem;">No hay propuestas registradas.</p>'
        : '';

    proposals.forEach(prop => {
        const div = document.createElement('div');
        div.className = 'proposal-item';

        let actionButton = '';
        if (isReceived && prop.status === 'PENDING') {
            actionButton = `<button onclick="openRespondModal(${prop.id})" class="btn-action btn-primary" style="margin-top:10px; padding: 4px 8px; font-size:0.8rem;">Responder</button>`;
        }

        div.innerHTML = `
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
                <span style="font-size:0.8rem; font-weight:bold; color:#64748b;">Propuesta #${prop.id}</span>
                <span class="proposal-status status-${prop.status.toLowerCase()}">${statusLabels[prop.status] || prop.status}</span>
            </div>
            <p style="font-size:0.85rem; color:#64748b;">De: <strong>${prop.senderName}</strong> · ${prop.createdAt || ''}</p>
            <p style="font-size:0.9rem; color:#334155;"><strong>Publicación deseada:</strong> ${prop.targetPublicationTitle}</p>
            <p style="font-size:0.9rem; color:#334155;"><strong>Ofrecido a cambio:</strong> ${prop.offeredPublicationTitle}</p>
            ${actionButton}
        `;
        container.appendChild(div);
    });
}

// ==========================================
// RF10: Aceptar / Rechazar / Contraofertar
// ==========================================
async function openRespondModal(id) {
    document.getElementById('respond-proposal-id').value = id;
    document.getElementById('respond-modal-text').textContent = `Vas a responder a la propuesta #${id}`;
    document.getElementById('respond-action').value = 'ACCEPTED';
    document.getElementById('counter-offer-group').classList.add('hidden');
    document.getElementById('respond-modal').classList.remove('hidden');
}

document.getElementById('btn-close-respond').addEventListener('click', () => {
    document.getElementById('respond-modal').classList.add('hidden');
});

// Muestra el selector de contraoferta y carga mis publicaciones cuando se elige esa opción
document.getElementById('respond-action').addEventListener('change', async (e) => {
    const counterGroup = document.getElementById('counter-offer-group');
    if (e.target.value === 'COUNTERED') {
        counterGroup.classList.remove('hidden');
        await loadMyPostsForCounter();
    } else {
        counterGroup.classList.add('hidden');
    }
});

async function loadMyPostsForCounter() {
    const select = document.getElementById('respond-counter-post');
    select.innerHTML = '<option value="" disabled selected>Cargando tus publicaciones...</option>';

    try {
        const response = await fetch(`${API_URL}/publicaciones/all`);
        if (!response.ok) throw new Error('Error al cargar publicaciones');

        const allPosts = await response.json();
        const myPosts = allPosts.filter(p => p.user && p.user.id == currentUserId);

        if (myPosts.length === 0) {
            select.innerHTML = '<option value="" disabled selected>No tienes publicaciones propias</option>';
            return;
        }

        select.innerHTML = '<option value="" disabled selected>Selecciona una publicación</option>';
        myPosts.forEach(p => {
            select.innerHTML += `<option value="${p.id}">${p.title}</option>`;
        });

    } catch (error) {
        console.error('Error cargando publicaciones propias:', error);
        select.innerHTML = '<option value="" disabled selected>Error al cargar</option>';
    }
}

document.getElementById('form-respond-proposal').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('respond-proposal-id').value;
    const status = document.getElementById('respond-action').value;

    let counterOfferedPublicationId = null;
    if (status === 'COUNTERED') {
        const counterPostId = document.getElementById('respond-counter-post').value;
        if (!counterPostId) {
            alert('Selecciona una publicación para tu contraoferta.');
            return;
        }
        counterOfferedPublicationId = parseInt(counterPostId);
    }

    const body = { status, counterOfferedPublicationId };

    try {
        const res = await fetch(`${API_URL}/propuestas/${id}/responder`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            alert('Respuesta procesada correctamente');
            document.getElementById('respond-modal').classList.add('hidden');
            loadSentProposals();
            loadReceivedProposals();
        } else {
            const data = await res.json().catch(() => null);
            const errorMsg = Array.isArray(data) ? data.join(', ') : (data || 'Hubo un error al procesar la respuesta');
            alert(errorMsg);
        }
    } catch (error) {
        alert('Error de conexión: ' + error.message);
    }
});