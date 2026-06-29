// Configuración de la API y Cloudinary
const API_URL = 'https://tradenow-437n.onrender.com/api';
const CLOUD_NAME = "sboc5dmc"; 
const UPLOAD_PRESET = "mi_preset_tradenow"; 

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

        const fileInput = document.getElementById('post-image-file');
        const file = fileInput.files[0];
        const userId = localStorage.getItem('userId');
        
        if (!userId || userId === "null") {
            showAlert('Debes iniciar sesión para crear una publicación.');
            return;
        }

        if (!file) {
            showAlert('Por favor, selecciona una imagen para el artículo.');
            return;
        }

        try {
            showAlert("Subiendo imagen a la nube...", true);

            // 1. Subir imagen a Cloudinary
            const formData = new FormData();
            formData.append("file", file);
            formData.append("upload_preset", UPLOAD_PRESET);

            const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`, {
                method: "POST",
                body: formData
            });

            if (!res.ok) throw new Error("Error al subir la imagen. Verifica tu configuración de Cloudinary.");
            
            const imageData = await res.json();
            const imageUrl = imageData.secure_url;

            // 2. Preparar datos con validación estricta
            const rawValue = document.getElementById('post-value').value;
            const postData = {
                title: document.getElementById('post-title').value,
                description: document.getElementById('post-description').value,
                imageUrl: imageUrl,
                type: document.getElementById('post-type').value, // Asegúrate que sea 'OFFER' o 'SEARCH'
                estimatedValue: rawValue && rawValue !== "" ? parseFloat(rawValue) : 0.0,
                exchangeFor: document.getElementById('post-exchange-for').value,
                userId: parseInt(userId),
                categoryId: parseInt(document.getElementById('post-category').value),
                zoneId: parseInt(document.getElementById('post-zone').value)
            };

            // 3. Enviar datos al backend
            showAlert("Guardando publicación...", true);
            const response = await fetch(`${API_URL}/publicaciones/new`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            });

            const responseText = await response.text();
            
            if (!response.ok) {
                // Aquí el backend nos dice exactamente qué validación falló
                throw new Error(responseText || 'Error en el servidor');
            }

            showAlert('¡Publicación creada con éxito!', true);
            formCreatePost.reset();
            setTimeout(() => window.location.href = 'dashboard.html', 2000);

        } catch (error) {
            showAlert("Error: " + error.message);
            console.error("Detalle del error:", error);
        }
    });
}