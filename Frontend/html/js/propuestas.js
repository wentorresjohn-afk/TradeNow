const currentUserId = localStorage.getItem('userId');
if (!currentUserId) window.location.href = 'login.html';

document.addEventListener('DOMContentLoaded', () => {
    loadSentProposals();
    loadReceivedProposals();
});

async function loadSentProposals() {
    const res = await fetch(`/api/propuestas/enviadas?userId=${currentUserId}`);
    if (res.ok) {
        const data = await res.json();
        renderProposals(data, 'enviadas-container', false);
    }
}

async function loadReceivedProposals() {
    const res = await fetch(`/api/propuestas/recibidas?userId=${currentUserId}`);
    if (res.ok) {
        const data = await res.json();
        renderProposals(data, 'recibidas-container', true);
    }
}

function renderProposals(proposals, containerId, isReceived) {
    const container = document.getElementById(containerId);
    container.innerHTML = proposals.length === 0 ? '<p style="color: #94a3b8; font-size: 0.9rem;">No hay propuestas registradas.</p>' : '';
    
    proposals.forEach(prop => {
        const div = document.createElement('div');
        div.className = 'proposal-item';
        
        let actionButton = '';
        if (isReceived && prop.status === 'PENDIENTE') {
            actionButton = `<button onclick="openRespondModal(${prop.id}, '${prop.description}')" class="btn-action btn-primary" style="margin-top:10px; padding: 4px 8px; font-size:0.8rem;">Responder</button>`;
        }

        div.innerHTML = `
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
                <span style="font-size:0.8rem; font-weight:bold; color:#64748b;">Propuesta #${prop.id}</span>
                <span class="proposal-status status-${prop.status.toLowerCase()}">${prop.status}</span>
            </div>
            <p style="font-size:0.9rem; color:#334155;"><strong>Ofrecido:</strong> ${prop.description}</p>
            ${actionButton}
        `;
        container.appendChild(div);
    });
}

function openRespondModal(id, desc) {
    document.getElementById('respond-proposal-id').value = id;
    document.getElementById('respond-modal-text').textContent = `Propuesta: "${desc}"`;
    document.getElementById('respond-modal').classList.remove('hidden');
}

document.getElementById('btn-close-respond').addEventListener('click', () => {
    document.getElementById('respond-modal').classList.add('hidden');
});

document.getElementById('form-respond-proposal').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('respond-proposal-id').value;
    const action = document.getElementById('respond-action').value;

    // Supongamos que tu ProposalResponseDTO mapea un campo String u Objeto estructurado según lo maneje tu backend
    const res = await fetch(`/api/propuestas/${id}/responder`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ decision: action }) // Estructurar según el DTO real
    });

    if (res.ok) {
        alert('Respuesta procesada correctamente');
        document.getElementById('respond-modal').classList.add('hidden');
        loadSentProposals();
        loadReceivedProposals();
    } else {
        alert('Hubo un error al procesar la respuesta');
    }
});