/*  vehicleForm.js
    ------------------------------------------------------------
    - Validación Bootstrap 5
    - Vista previa de imagen
    ------------------------------------------------------------ */

(() => {
  'use strict';

  document.addEventListener('DOMContentLoaded', () => {
    /* ---------- 1. VALIDACIÓN BOOTSTRAP ---------- */
    const forms = document.querySelectorAll('.needs-validation');

    forms.forEach(form => {
      form.addEventListener('submit', event => {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        
        form.classList.add('was-validated');
      }, false);
    });

    /* ---------- 2. PREVISUALIZACIÓN DE IMAGEN ---------- */
    const photoInput = document.getElementById('vehiclePhoto');
    const photoPreview = document.getElementById('photoPreview');

    if (photoInput && photoPreview) {
      photoInput.addEventListener('change', () => {
        const file = photoInput.files[0];
        if (file) {
          photoPreview.src = URL.createObjectURL(file);
          photoPreview.classList.remove('d-none');
        } else {
          photoPreview.classList.add('d-none');
        }
      });
    }
  });
})();