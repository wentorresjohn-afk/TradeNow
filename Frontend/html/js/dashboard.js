function renderPosts(posts) {
    postsContainer.innerHTML = '';
    posts.forEach(post => {
        const card = document.createElement('div');
        card.className = 'post-card'; // Clase CSS nativa
        
        card.innerHTML = `
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
