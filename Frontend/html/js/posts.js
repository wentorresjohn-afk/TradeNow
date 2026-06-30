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

// 1. CARGA DINÁMICA: Llena los selects al abrir la página
document.addEventListener('DOMContentLoaded', async () => {
    const typeSelect = document.getElementById('post-type');
    const catSelect = document.getElementById('post-category');
    const zoneSelect = document.getElementById('post-zone');

    // Llenar tipos fijos
    typeSelect.innerHTML = `<option value="" disabled selected>Selecciona una opción</option>
                            <option value="OFFER">Oferta</option>
                            <option value="SEARCH">Búsqueda</option>`;

    try {
        const [catRes, zoneRes] = await Promise.all([
            fetch(`${API_URL}/categorias`),
            fetch(`${API_URL}/zonas`)
        ]);
        
        const categorias = await catRes.json();
        const zonas = await zoneRes.json();

        catSelect.innerHTML = '<option value="" disabled selected>Selecciona una categoría</option>';
        categorias.forEach(c => catSelect.innerHTML += `<option value="${c.id}">${c.name}</option>`);

        zoneSelect.innerHTML = '<option value="" disabled selected>Selecciona una zona</option>';
        zonas.forEach(z => zoneSelect.innerHTML += `<option value="${z.id}">${z.name}</option>`);
    } catch (e) {
        console.error("Error al cargar selects:", e);
        showAlert("Error cargando opciones. Revisa tu conexión.");
    }
});

// 2. LÓGICA DE ENVÍO
if (formCreatePost) {
    formCreatePost.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        // Limpiar alerta anterior
        alertContainer.style.display = 'none';

        const fileInput = document.getElementById('post-image-file');
        const file = fileInput.files[0];
        const userId = localStorage.getItem('userId');
        
        // Validación de Tipo antes de empezar
        const typeValue = document.getElementById('post-type').value;
        if (!typeValue) {
            showAlert('Por favor, selecciona un tipo de publicación válido.');
            return;
        }

        if (!userId || userId === "null") {
            showAlert('Debes iniciar sesión.');
            return;
        }

        try {
            showAlert("Subiendo imagen a la nube...", true);

            const formData = new FormData();
            formData.append("file", file);
            formData.append("upload_preset", UPLOAD_PRESET);

            const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`, {
                method: "POST", body: formData
            });

            if (!res.ok) throw new Error("Error al subir la imagen.");
            
            const imageData = await res.json();

            const rawValue = document.getElementById('post-value').value;
            const postData = {
                title: document.getElementById('post-title').value,
                description: document.getElementById('post-description').value,
                imageUrl: imageData.secure_url,
                type: typeValue, // Ya validado arriba
                estimatedValue: rawValue && rawValue !== "" ? parseFloat(rawValue) : 0.0,
                exchangeFor: document.getElementById('post-exchange-for').value,
                userId: parseInt(userId),
                categoryId: parseInt(document.getElementById('post-category').value),
                zoneId: parseInt(document.getElementById('post-zone').value)
            };

            showAlert("Guardando publicación...", true);
            const response = await fetch(`${API_URL}/publicaciones/new`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postData)
            });

            // Si el backend devuelve error, leemos el mensaje exacto
            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg || "Error al crear la publicación");
            }

            showAlert('¡Publicación creada con éxito!', true);
            formCreatePost.reset();
            setTimeout(() => window.location.href = 'dashboard.html', 2000);

        } catch (error) {
            showAlert("Error: " + error.message);
            console.error("Detalle:", error);
        }
    });
}