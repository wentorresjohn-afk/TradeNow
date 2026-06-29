const API_URL = 'https://tradenow-437n.onrender.com/api';
const formCreatePost = document.getElementById('form-create-post');
const alertContainer = document.getElementById('post-alert');

function showAlert(message, isSuccess = false) {
    alertContainer.textContent = message;
    alertContainer.className = `alert ${isSuccess ? 'success' : 'error'}`;
    alertContainer.style.display = 'block';
}

if (formCreatePost) {
    formCreatePost.addEventListener('submit', async (e) => {
        e.preventDefault();

        // 1. Validar sesión del usuario desde el almacenamiento local
        const userId = localStorage.getItem('userId');
        
        if (!userId) {
            showAlert('Debes iniciar sesión para crear una publicación.');
            setTimeout(() => window.location.href = 'login.html', 2000);
            return;
        }

        // 2. Construir el DTO respetando los tipos y nombres exactos del backend (PostsDTO)
        // Nota: Asegúrate de que los valores numéricos vengan como enteros
        const postData = {
            type: document.getElementById('post-type').value, // STRING (Enum en Java)
            title: document.getElementById('post-title').value,
            description: document.getElementById('post-description').value,
            estimatedValue: parseFloat(document.getElementById('post-value').value) || 0.0,
            exchangeFor: document.getElementById('post-exchange-for').value,
            imageUrl: document.getElementById('post-image-url').value, // Nuevo campo
            userId: parseInt(userId),
            categoryId: parseInt(document.getElementById('post-category').value),
            zoneId: parseInt(document.getElementById('post-zone').value)
        };

        try {
            // 3. Petición al endpoint: /api/publicaciones/new
            const response = await fetch(`${API_URL}/publicaciones/new`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json' 
                },
                body: JSON.stringify(postData)
            });

            // 4. Procesar respuesta
            const contentType = response.headers.get("content-type");
            let data;
            
            if (contentType && contentType.includes("application/json")) {
                data = await response.json();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                // Manejar errores de validación (BindingResult)
                const errorMsg = Array.isArray(data) ? data.join(', ') : (data || 'Error al crear la publicación');
                throw new Error(errorMsg);
            }

            showAlert('¡Publicación creada con éxito!', true);
            formCreatePost.reset();
            
            // Redirigir al dashboard tras 2 segundos
            setTimeout(() => window.location.href = 'dashboard.html', 2000);

        } catch (error) {
            showAlert(error.message);
        }
    });
}
