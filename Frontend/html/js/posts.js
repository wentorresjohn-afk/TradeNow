const API_URL = 'https://tradenow-437n.onrender.com/api';
const formCreatePost = document.getElementById('form-create-post');
const alertContainer = document.getElementById('post-alert');

function showAlert(message, isSuccess = false) {
    alertContainer.textContent = message;
    alertContainer.className = `alert ${isSuccess ? 'success' : 'error'}`;
}

if (formCreatePost) {
    formCreatePost.addEventListener('submit', async (e) => {
        e.preventDefault();

        // 1. Validar sesión del usuario
        const userId = localStorage.getItem('userId');
        
        if (!userId) {
            showAlert('Debes iniciar sesión para crear una publicación.');
            setTimeout(() => window.location.href = 'login.html', 2000);
            return;
        }

        // 2. Construir el DTO respetando tipos y nombres del backend (PostsDTO)
        const postData = {
            type: document.getElementById('post-type').value, // Enum
            title: document.getElementById('post-title').value,
            description: document.getElementById('post-description').value,
            estimatedValue: parseFloat(document.getElementById('post-value').value) || 0.0, // BigDecimal
            exchangeFor: document.getElementById('post-exchange-for').value,
            userId: parseInt(userId), // Entero
            categoryId: parseInt(document.getElementById('post-category').value), // Entero
            zoneId: parseInt(document.getElementById('post-zone').value) // Entero
        };

        try {
            // 3. Apuntamos al controlador exacto: @PostMapping("/new")
            const response = await fetch(`${API_URL}/publicaciones/new`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            });

            // Si el servidor devuelve texto (ej. "Publicación exitosa" o error de Render dormido)
            const contentType = response.headers.get("content-type");
            let data;
            
            if (contentType && contentType.includes("application/json")) {
                data = await response.json();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                // Manejar los errores de validación del BindingResult de Java
                const errorMsg = Array.isArray(data) ? data.join(', ') : (data || 'Error al crear la publicación');
                throw new Error(errorMsg);
            }

            showAlert('¡Publicación creada con éxito!', true);
            formCreatePost.reset();
            
            // Redirigir al dashboard
            setTimeout(() => window.location.href = 'dashboard.html', 2000);

        } catch (error) {
            showAlert(error.message);
        }
    });
}