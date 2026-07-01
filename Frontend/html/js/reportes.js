const API_URL = 'https://tradenow-437n.onrender.com/api';

// ==========================================
// Control de acceso: solo administradores
// ==========================================
document.addEventListener('DOMContentLoaded', async () => {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const res = await fetch(`${API_URL}/user/${userId}`);
        if (!res.ok) throw new Error('No se pudo verificar el usuario');

        const user = await res.json();

        if (user.userType !== 'ADMINISTRATOR') {
            alert('No tienes permisos para acceder a esta sección.');
            window.location.href = 'dashboard.html';
            return;
        }

        // Si es administrador, carga los reportes normalmente
        loadGeneralReports();

    } catch (error) {
        console.error('Error verificando permisos:', error);
        alert('Ocurrió un error al verificar tus permisos.');
        window.location.href = 'dashboard.html';
    }
});

// ==========================================
// RF17: Categorías más intercambiadas (con rango de fechas)
// ==========================================
async function loadCategoryReport() {
    const start = document.getElementById('rep-start').value;
    const end = document.getElementById('rep-end').value;
    const tbody = document.querySelector('#table-categories tbody');

    if (!start || !end) {
        alert('Defina el rango de fechas');
        return;
    }

    tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Cargando...</td></tr>';

    try {
        const res = await fetch(`${API_URL}/reportes/categorias?startDate=${start}&endDate=${end}`);

        if (!res.ok) {
            const errorMsg = await res.text();
            throw new Error(errorMsg || 'Error al generar el reporte');
        }

        const data = await res.json();
        tbody.innerHTML = '';

        if (!data || data.length === 0) {
            tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">No hay intercambios en ese rango de fechas.</td></tr>';
            return;
        }

        data.forEach(item => {
            tbody.innerHTML += `<tr><td>${item.categoryName}</td><td>${item.count}</td></tr>`;
        });

    } catch (error) {
        console.error('Error cargando reporte de categorías:', error);
        tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Ocurrió un error al generar el reporte.</td></tr>';
    }
}

// ==========================================
// RF18: Usuarios más activos, trueques por zona, valor total
// ==========================================
async function loadGeneralReports() {
    await Promise.all([
        loadActiveUsers(),
        loadZoneReport(),
        loadTotalValue()
    ]);
}

async function loadActiveUsers() {
    const tbody = document.querySelector('#table-users tbody');
    tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Cargando...</td></tr>';

    try {
        const res = await fetch(`${API_URL}/reportes/usuarios-activos`);
        if (!res.ok) throw new Error('Error al cargar usuarios activos');

        const users = await res.json();
        tbody.innerHTML = '';

        if (!users || users.length === 0) {
            tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Aún no hay actividad registrada.</td></tr>';
            return;
        }

        users.forEach(u => {
            tbody.innerHTML += `<tr><td>${u.userName}</td><td>${u.proposalCount}</td></tr>`;
        });

    } catch (error) {
        console.error('Error cargando usuarios activos:', error);
        tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Ocurrió un error al cargar los datos.</td></tr>';
    }
}

async function loadZoneReport() {
    const tbody = document.querySelector('#table-zones tbody');
    tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Cargando...</td></tr>';

    try {
        const res = await fetch(`${API_URL}/reportes/por-zona`);
        if (!res.ok) throw new Error('Error al cargar trueques por zona');

        const zones = await res.json();
        tbody.innerHTML = '';

        if (!zones || zones.length === 0) {
            tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Aún no hay trueques completados.</td></tr>';
            return;
        }

        zones.forEach(z => {
            tbody.innerHTML += `<tr><td>${z.cityName}</td><td>${z.count}</td></tr>`;
        });

    } catch (error) {
        console.error('Error cargando reporte por zona:', error);
        tbody.innerHTML = '<tr class="report-empty-row"><td colspan="2">Ocurrió un error al cargar los datos.</td></tr>';
    }
}

async function loadTotalValue() {
    const txtTotal = document.getElementById('txt-total-value');

    try {
        const res = await fetch(`${API_URL}/reportes/valor-total`);
        if (!res.ok) throw new Error('Error al cargar el valor total');

        const val = await res.json();
        const formatted = Number(val.totalValue || 0).toLocaleString('es-CR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });

        txtTotal.textContent = `💵 ₡${formatted}`;

    } catch (error) {
        console.error('Error cargando valor total:', error);
        txtTotal.textContent = '💵 No disponible';
    }
}