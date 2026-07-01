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

// ==========================
// CARGAR SELECTS
// ==========================
document.addEventListener('DOMContentLoaded', async () => {

    const typeSelect = document.getElementById('post-type');
    const catSelect = document.getElementById('post-category');
    const zoneSelect = document.getElementById('post-zone');

    // Tipos
    typeSelect.innerHTML = `
        <option value="" disabled selected>Selecciona una opción</option>
        <option value="OFFER">Oferta</option>
        <option value="SEARCH">Búsqueda</option>
    `;

    try {

        const [catRes, zoneRes] = await Promise.all([
            fetch(`${API_URL}/publicaciones/categorias`),
            fetch(`${API_URL}/publicaciones/zonas`)
        ]);

        if (!catRes.ok || !zoneRes.ok) {
            throw new Error("No fue posible cargar la información.");
        }

        const categorias = await catRes.json();
        const zonas = await zoneRes.json();

        // ==========================
        // CATEGORÍAS
        // ==========================

        catSelect.innerHTML =
            '<option value="" disabled selected>Selecciona una categoría</option>';

        categorias.forEach(c => {

            catSelect.innerHTML += `
                <option value="${c.id}">
                    ${c.name}
                </option>
            `;

        });

        // ==========================
        // CIUDADES
        // ==========================

        zoneSelect.innerHTML =
            '<option value="" disabled selected>Selecciona una ciudad</option>';

        zonas.forEach(z => {

            zoneSelect.innerHTML += `
                <option value="${z.id}">
                    ${z.cityName}
                </option>
            `;

            // Si prefieres mostrar provincia también usa:
            // ${z.cityName} - ${z.state}

        });

    } catch (e) {

        console.error(e);
        showAlert("Error cargando categorías y ciudades.");

    }

});

// ==========================
// CREAR PUBLICACIÓN
// ==========================

if (formCreatePost) {

    formCreatePost.addEventListener('submit', async (e) => {

        e.preventDefault();

        alertContainer.style.display = "none";

        const fileInput = document.getElementById('post-image-file');
        const file = fileInput.files[0];

        if (!file) {
            showAlert("Debes seleccionar una imagen.");
            return;
        }

        const userId = localStorage.getItem("userId");

        if (!userId || userId === "null") {
            showAlert("Debes iniciar sesión.");
            return;
        }

        const typeValue = document.getElementById("post-type").value;

        if (!typeValue) {
            showAlert("Selecciona un tipo de publicación.");
            return;
        }

        try {

            showAlert("Subiendo imagen...", true);

            const formData = new FormData();

            formData.append("file", file);
            formData.append("upload_preset", UPLOAD_PRESET);

            const uploadResponse = await fetch(
                `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`,
                {
                    method: "POST",
                    body: formData
                }
            );

            if (!uploadResponse.ok) {
                throw new Error("No se pudo subir la imagen.");
            }

            const imageData = await uploadResponse.json();

            const rawValue = document.getElementById("post-value").value;

            const postData = {

                title: document.getElementById("post-title").value,

                description: document.getElementById("post-description").value,

                imageUrl: imageData.secure_url,

                type: typeValue,

                estimatedValue:
                    rawValue !== ""
                        ? parseFloat(rawValue)
                        : 0.0,

                exchangeFor: document.getElementById("post-exchange-for").value,

                userId: parseInt(userId),

                categoryId: parseInt(document.getElementById("post-category").value),

                zoneId: parseInt(document.getElementById("post-zone").value)

            };

            showAlert("Guardando publicación...", true);

            const response = await fetch(`${API_URL}/publicaciones/new`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(postData)

            });

            if (!response.ok) {

                const error = await response.text();

                throw new Error(error || "No se pudo crear la publicación.");

            }

            showAlert("¡Publicación creada con éxito!", true);

            formCreatePost.reset();

            setTimeout(() => {

                window.location.href = "dashboard.html";

            }, 2000);

        } catch (error) {

            console.error(error);

            showAlert(error.message);

        }

    });

}