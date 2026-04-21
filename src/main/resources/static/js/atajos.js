document.addEventListener('keydown', function (event) {
    const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
    const ctrlKey = isMac ? event.metaKey : event.ctrlKey;

    if (ctrlKey && event.key.toLowerCase() === 'b') {
        event.preventDefault();
        const buscador = document.getElementById('busquedaRapida');
        if (buscador) {
            buscador.style.display = 'block';
            const input = buscador.querySelector('input');
            if (input) input.focus();
        }
    }

    if (ctrlKey && event.key === '/') {
        event.preventDefault();
        const el = document.getElementById('modalAtajos');
        if (el) new bootstrap.Modal(el).show();
    }

    if (event.key === 'Escape') {
        const buscador = document.getElementById('busquedaRapida');
        if (buscador) buscador.style.display = 'none';
    }
});
