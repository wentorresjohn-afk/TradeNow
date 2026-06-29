const API_URL = 'https://tradenow-437n.onrender.com/api'


// Elementos de la Interfaz de Usuario (UI)
const tabLogin = document.getElementById('tab-login');
const tabRegister = document.getElementById('tab-register');
const formLogin = document.getElementById('form-login');
const formRegister = document.getElementById('form-register');
const alertContainer = document.getElementById('alert-container');

// ==========================================
// 1. CONTROL DE PESTAÑAS (LOGIN / REGISTRO)
// ==========================================
if (tabLogin && tabRegister && formLogin && formRegister) {
    
    // Acción al hacer clic en "Iniciar Sesión"
    tabLogin.addEventListener('click', () => {
        tabLogin.classList.add('active');
        tabRegister.classList.remove('active');
        formLogin.classList.remove('hidden');
        formRegister.classList.add('hidden');
        if (alertContainer) alertContainer.classList.add('hidden');
    });

    // Acción al hacer clic en "Registrarse"
    tabRegister.addEventListener('click', () => {
        tabRegister.classList.add('active');
        tabLogin.classList.remove('active');
        formRegister.classList.remove('hidden');
        formLogin.classList.add('hidden');
        if (alertContainer) alertContainer.classList.add('hidden');
    });

} else {
    console.error("Error crítico: No se encontraron todos los IDs requeridos en el HTML (tab-login, tab-register, form-login, form-register).");
}

// ==========================================
// 2. SISTEMA DE ALERTAS NATIVO
// ==========================================
function showAlert(messages, isSuccess = false) {
    if (!alertContainer) return;

    alertContainer.innerHTML = '';
    alertContainer.classList.remove('hidden', 'error', 'success');
    
    // Aplicar clase según el tipo de alerta
    if (isSuccess) {
        alertContainer.classList.add('success');
    } else {
        alertContainer.classList.add('error');
    }

    // Si viene una lista de errores (Validaciones de Spring Boot BindingResult)
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
        // Si es un string simple
        alertContainer.textContent = messages;
    }
}

// ==========================================
// 3. PROCESAR FORMULARIO DE INICIO DE SESIÓN
// ==========================================
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

        // Validar que el backend responda con JSON y no con un documento HTML de error
        const contentType = response.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error("El servidor respondió con un formato inesperado (HTML/Texto). Verifica que la ruta '/api/aut/login' esté bien mapeada.");
        }

        const data = await response.json();

        if (!response.ok) {
            const errorMsg = Array.isArray(data) ? data : (data.message || 'Credenciales inválidas');
            throw new Error(JSON.stringify(errorMsg));
        }

        // Guardar sesión local
        localStorage.setItem('userId', data.id);
        localStorage.setItem('userName', data.name || 'Usuario');
        
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

// ==========================================
// 4. PROCESAR FORMULARIO DE REGISTRO
// ==========================================
formRegister.addEventListener('submit', async (e) => {
    e.preventDefault();

    const regData = {
        name: document.getElementById('reg-name').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value,
        phone: document.getElementById('reg-phone').value
    };

    try {
        const response = await fetch(`${API_URL}/aut/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(regData)
        });

        // Validar que el backend responda con JSON y no con un documento HTML de error
        const contentType = response.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            throw new Error("El servidor respondió con un formato inesperado (HTML/Texto). Verifica el mapeo de '/api/aut/register' en tu Java.");
        }

        const data = await response.json();

        if (response.status === 400 || !response.ok) {
            const errorMsg = Array.isArray(data) ? data : (data.message || 'Error en el registro');
            throw new Error(JSON.stringify(errorMsg));
        }

        showAlert('Registro completado con éxito. Ya puedes iniciar sesión.', true);
        formRegister.reset();
        
        // Retornar automáticamente a la pestaña de Login tras registrarse
        setTimeout(() => {
            tabLogin.click();
        }, 2000);

    } catch (error) {
        try {
            const parsedError = JSON.parse(error.message);
            showAlert(parsedError);
        } catch {
            showAlert(error.message);
        }
    }
});