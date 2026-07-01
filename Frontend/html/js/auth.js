const API_URL = 'https://tradenow-437n.onrender.com/api';

const tabLogin = document.getElementById('tab-login');
const tabRegister = document.getElementById('tab-register');
const formLogin = document.getElementById('form-login');
const formRegister = document.getElementById('form-register');
const alertContainer = document.getElementById('alert-container');

if (tabLogin && tabRegister && formLogin && formRegister) {
    tabLogin.addEventListener('click', () => {
        tabLogin.classList.add('active');
        tabRegister.classList.remove('active');
        formLogin.classList.remove('hidden');
        formRegister.classList.add('hidden');
        if (alertContainer) alertContainer.classList.add('hidden');
    });

    tabRegister.addEventListener('click', () => {
        tabRegister.classList.add('active');
        tabLogin.classList.remove('active');
        formRegister.classList.remove('hidden');
        formLogin.classList.add('hidden');
        if (alertContainer) alertContainer.classList.add('hidden');
    });
} else {
    console.error("Error crítico: No se encontraron todos los IDs requeridos en el HTML.");
}

function showAlert(messages, isSuccess = false) {
    if (!alertContainer) return;
    alertContainer.innerHTML = '';
    alertContainer.classList.remove('hidden', 'error', 'success');
    alertContainer.classList.add(isSuccess ? 'success' : 'error');

    if (Array.isArray(messages)) {
        const ul = document.createElement('ul');
        ul.style.paddingLeft = '20px';
        ul.style.margin = '0';
        messages.forEach(msg => {
            const li = document.createElement('li');
            li.textContent = msg;
            ul.appendChild(li);
        });
        alertContainer.appendChild(ul);
    } else {
        alertContainer.textContent = messages;
    }
}

formLogin.addEventListener('submit', async (e) => {
    e.preventDefault();

    const loginData = {
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-password').value
    };

    try {
        const response = await fetch(`${API_URL}/aut/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(loginData)
        });

        const contentType = response.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error("El servidor respondió con un formato inesperado. Asegúrate de que el backend esté despierto.");
        }

        const data = await response.json();

        if (!response.ok) {
            const errorMsg = Array.isArray(data) ? data : (data.message || 'Credenciales inválidas');
            throw new Error(JSON.stringify(errorMsg));
        }

        localStorage.setItem('userId', data.id);
        localStorage.setItem('userName', data.name || 'Usuario');
        localStorage.setItem('userType', data.userType);

        showAlert('¡Ingreso exitoso! Redirigiendo...', true);
        setTimeout(() => window.location.href = 'dashboard.html', 1500);

    } catch (error) {
        try {
            const parsedError = JSON.parse(error.message);
            showAlert(parsedError);
        } catch {
            showAlert(error.message);
        }
    }
});

formRegister.addEventListener('submit', async (e) => {
    e.preventDefault();

    const regData = {
        name: document.getElementById('reg-name').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value,
        geographicZone: document.getElementById('reg-zone').value,
        userType: document.getElementById('reg-usertype').value
    };

    try {
        const response = await fetch(`${API_URL}/aut/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(regData)
        });

        const contentType = response.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error("El servidor respondió con un formato inesperado.");
        }

        const data = await response.json();

        if (!response.ok) {
            const errorMsg = Array.isArray(data) ? data : (data.message || 'Error en el registro');
            throw new Error(JSON.stringify(errorMsg));
        }

        showAlert('Registro completado con éxito. Ya puedes iniciar sesión.', true);
        formRegister.reset();
        setTimeout(() => tabLogin.click(), 2000);

    } catch (error) {
        try {
            const parsedError = JSON.parse(error.message);
            showAlert(parsedError);
        } catch {
            showAlert(error.message);
        }
    }
});