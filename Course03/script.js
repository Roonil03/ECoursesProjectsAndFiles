document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.querySelector('.menu-toggle');
    const navBar = document.getElementById('primary-nav');

    toggleBtn.addEventListener('click', () => {
        const isOpen = navBar.classList.toggle('active');
        toggleBtn.setAttribute('aria-expanded', isOpen);
    });

    document.querySelectorAll('nav a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            navBar.classList.remove('active');
            toggleBtn.setAttribute('aria-expanded', 'false');
            
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });

    const filterBtns = document.querySelectorAll('.filter-btn');
    const projectCards = document.querySelectorAll('.project-card');

    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            const category = btn.getAttribute('data-category');
            
            projectCards.forEach(card => {
                if (category === 'all' || card.getAttribute('data-category') === category) {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });

    const modal = document.getElementById('lightbox-modal');
    const modalImg = document.getElementById('modal-img');
    const closeBtn = document.querySelector('.modal-close');

    document.querySelectorAll('.project-card figure img').forEach(img => {
        img.addEventListener('click', () => {
            modal.style.display = 'flex';
            modal.setAttribute('aria-hidden', 'false');
            modalImg.src = img.src;
            modalImg.alt = img.alt;
        });
    });

    const closeModal = () => {
        modal.style.display = 'none';
        modal.setAttribute('aria-hidden', 'true');
    };

    closeBtn.addEventListener('click', closeModal);
    modal.addEventListener('click', (e) => {
        if (e.target === modal) closeModal();
    });

    const form = document.getElementById('contact-form');
    const nameInput = document.getElementById('user-name');
    const emailInput = document.getElementById('user-email');
    const messageInput = document.getElementById('user-message');
    const statusDiv = document.getElementById('form-status');

    const inputs = [
        { el: nameInput, err: document.getElementById('name-error'), msg: 'Name field cannot be left blank.' },
        { el: emailInput, err: document.getElementById('email-error'), msg: 'Please provide a valid email format.' },
        { el: messageInput, err: document.getElementById('message-error'), msg: 'Message block cannot be left blank.' }
    ];

    const validateInput = (input) => {
        let valid = true;
        if (!input.el.value.trim()) {
            input.err.textContent = input.msg;
            valid = false;
        } else if (input.el.type === 'email' && !/\S+@\S+\.\S+/.test(input.el.value)) {
            input.err.textContent = input.msg;
            valid = false;
        } else {
            input.err.textContent = '';
        }
        return valid;
    };

    inputs.forEach(input => {
        input.el.addEventListener('input', () => validateInput(input));
    });

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        let formValid = true;

        inputs.forEach(input => {
            if (!validateInput(input)) formValid = false;
        });

        if (formValid) {
            statusDiv.style.color = 'var(--success-color)';
            statusDiv.textContent = 'Message successfully dispatched.';
            form.reset();
        } else {
            statusDiv.style.color = 'var(--error-color)';
            statusDiv.textContent = 'Submission rejected. Correct outstanding field input errors.';
        }
    });
});
