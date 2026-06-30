const API_URL = 'https://tradenow-437n.onrender.com/api';
const CLOUD_NAME = "sboc5dmc";
const UPLOAD_PRESET = "mi_preset_tradenow";

const formEditProfile = document.getElementById('form-edit-profile');
const alertContainer = document.getElementById('edit-alert');
const photoPreview = document.getElementById('photo-preview');
const photoFileInput = document.getElementById('profile-photo-file');

let currentPhotoUrl = ''; // Guarda la foto actual por si el usuario no sube una nueva

function showAlert(message, isSuccess = false) {
    alertContainer.textContent = message;
    alertContainer.className = `alert ${isSuccess ? 'success' : 'error'}`;
    alertContainer.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', async () => {
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');

    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    const userDisplay = document.getElementById('user-display-name');
    if (userDisplay) {
        userDisplay.textContent = `Usuario: ${userName}`;
    }

    const logoutBtn = document.getElementById('btn-session-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = 'login.html';
        });
    }

    // Precargar los datos actuales del usuario
    await loadCurrentProfile(userId);
});

async function loadCurrentProfile(userId) {
    try {
        const response = await fetch(`${API_URL}/user/${userId}`);

        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar el perfil`);
        }

        const user = await response.json();

        currentPhotoUrl = user.photo || '';
        photoPreview.src = currentPhotoUrl || 'https://via.placeholder.com/100';
        document.getElementById('profile-description').value = user.description || '';
        document.getElementById('profile-zone').value = user.geographicZone || '';

    } catch (error) {
        console.error('Error cargando perfil actual:', error);
        showAlert('No se pudo cargar tu información actual.');
    }
}

// Vista previa instantánea al elegir una nueva foto
photoFileInput.addEventListener('change', () => {
    const file = photoFileInput.files[0];
    if (file) {
        photoPreview.src = URL.createObjectURL(file);
    }
});

formEditProfile.addEventListener('submit', async (e) => {
    e.preventDefault();
    alertContainer.style.display = 'none';

    const userId = localStorage.getItem('userId');
    if (!userId || userId === "null") {
        showAlert('Debes iniciar sesión.');
        return;
    }

    try {
        let finalPhotoUrl = currentPhotoUrl;
        const file = photoFileInput.files[0];

        // Si el usuario seleccionó una foto nueva, la subimos a Cloudinary
        if (file) {
            showAlert("Subiendo foto a la nube...", true);

            const formData = new FormData();
            formData.append("file", file);
            formData.append("upload_preset", UPLOAD_PRESET);

            const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`, {
                method: "POST", body: formData
            });

            if (!res.ok) throw new Error("Error al subir la foto.");

            const imageData = await res.json();
            finalPhotoUrl = imageData.secure_url;
        }

        const updateData = {
            photo: finalPhotoUrl,
            description: document.getElementById('profile-description').value,
            geographicZone: document.getElementById('profile-zone').value
        };

        showAlert("Guardando cambios...", true);

        const response = await fetch(`${API_URL}/user/${userId}/update`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateData)
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || "Error al actualizar el perfil");
        }

        showAlert('¡Perfil actualizado con éxito!', true);
        setTimeout(() => window.location.href = 'perfil.html', 1500);

    } catch (error) {
        showAlert("Error: " + error.message);
        console.error("Detalle:", error);
    }
});