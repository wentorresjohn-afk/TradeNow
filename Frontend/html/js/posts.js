const API_URL = 'https://tradenow-437n.onrender.com/api';

// --- CONFIGURACIÓN DE CLOUDINARY ---
// Sustituye estos valores con los de tu cuenta gratuita de Cloudinary
// --- CONFIGURACIÓN DE CLOUDINARY ---
const CLOUD_NAME = "sboc5dmc"; 
const UPLOAD_PRESET = "mi_preset_tradenow"; // O el nombre que tú le hayas puesto
// ------------------------------------
// ------------------------------------

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

        // 1. Obtener archivo y validar sesión
        const fileInput = document.getElementById('post-image-file');
        const file = fileInput.files[0];
        const userId = localStorage.getItem('userId');
        
        if (!userId) {
            showAlert('Debes iniciar sesión para crear una publicación.');
            return;
        }

        if (!file) {
            showAlert('Por favor, selecciona una imagen para el artículo.');
            return;
        }

        try {
            showAlert("Procesando imagen y subiendo a la nube...", true);

            // 2. Subir imagen a Cloudinary
            const formData = new FormData();
            formData.append("file", file);
            formData.append("upload_preset", UPLOAD_PRESET);
            formData.append("cloud_name", CLOUD_NAME);

            const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`, {
                method: "POST",
                body: formData
            });

            if (!res.ok) throw new Error("Error al subir la imagen a la nube.");
            
            const imageData = await res.json();
            const imageUrl = imageData.secure_url; // Esta es la URL que tu Java guardará

            // 3. Construir el DTO completo para tu backend
            const postData = {
                type: document.getElementById('post-type').value,
                title: document.getElementById('post-title').value,
                description: document.getElementById('post-description').value,
                estimatedValue: parseFloat(document.getElementById('post-value').value) || 0.0,
                exchangeFor: document.getElementById('post-exchange-for').value,
                imageUrl: imageUrl, // Aquí enviamos la URL recibida
                userId: parseInt(userId),
                categoryId: parseInt(document.getElementById('post-category').value),
                zoneId: parseInt(document.getElementById('post-zone').value)
            };

            // 4. Enviar datos a tu backend Java
            const response = await fetch(`${API_URL}/publicaciones/new`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            });

            const responseData = await response.text();

            if (!response.ok) {
                throw new Error(responseData || 'Error al guardar la publicación en la base de datos');
            }

            showAlert('¡Publicación creada con éxito!', true);
            formCreatePost.reset();
            
            // Redirigir al dashboard
            setTimeout(() => window.location.href = 'dashboard.html', 2000);

        } catch (error) {
            showAlert("Error: " + error.message);
        }
    });
}