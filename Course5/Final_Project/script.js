document.addEventListener('DOMContentLoaded', function() {
    const navLinks = document.querySelectorAll('.nav-menu a[href^="#"]');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            if (targetSection) {
                targetSection.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}
document.getElementById('recommendation-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const name = document.getElementById('recommender-name').value.trim();
    const position = document.getElementById('recommender-position').value.trim();
    const text = document.getElementById('recommendation-text').value.trim();
    if (!name || !position || !text) {
        alert('Please fill in all fields');
        return;
    }
    const recommendationCard = document.createElement('div');
    recommendationCard.className = 'recommendation-card';
    recommendationCard.style.opacity = '0';
    recommendationCard.style.transform = 'translateY(20px)';
    recommendationCard.innerHTML = `
        <div class="recommendation-header">
            <h4>${escapeHtml(name)}</h4>
            <span>${escapeHtml(position)}</span>
        </div>
        <p>"${escapeHtml(text)}"</p>
    `;
    const recommendationsList = document.getElementById('recommendations-list');
    recommendationsList.appendChild(recommendationCard);
    setTimeout(() => {
        recommendationCard.style.transition = 'all 0.5s ease';
        recommendationCard.style.opacity = '1';
        recommendationCard.style.transform = 'translateY(0)';
    }, 100);
    this.reset();
    showPopup();
});

function showPopup() {
    const modal = document.getElementById('popup-modal');
    modal.style.display = 'block';
    window.onclick = function(event) {
        if (event.target === modal) {
            closePopup();
        }
    }
}

function closePopup() {
    const modal = document.getElementById('popup-modal');
    modal.style.display = 'none';
    window.onclick = null;
}

function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}

window.addEventListener('scroll', function() {
    const sections = document.querySelectorAll('section[id]');
    const navLinks = document.querySelectorAll('.nav-menu a[href^="#"]');
    
    let current = '';
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop - 100;
        const sectionHeight = section.clientHeight;
        if (pageYOffset >= sectionTop && pageYOffset < sectionTop + sectionHeight) {
            current = section.getAttribute('id');
        }
    });
    
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === '#' + current) {
            link.classList.add('active');
        }
    });
});

const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver(function(entries) {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = '1';
            entry.target.style.transform = 'translateY(0)';
        }
    });
}, observerOptions);

document.addEventListener('DOMContentLoaded', function() {
    const animatedElements = document.querySelectorAll('.skill-item, .project-card, .recommendation-card');
    
    animatedElements.forEach(element => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(30px)';
        element.style.transition = 'all 0.6s ease';
        observer.observe(element);
    });
});

document.getElementById('recommender-name').addEventListener('blur', function() {
    validateField(this);
});

document.getElementById('recommender-position').addEventListener('blur', function() {
    validateField(this);
});

document.getElementById('recommendation-text').addEventListener('blur', function() {
    validateField(this);
});

function validateField(field) {
    const value = field.value.trim();
    const isValid = value.length > 0;
    
    if (!isValid) {
        field.style.borderColor = '#ff4757';
        field.style.backgroundColor = '#fff5f5';
    } else {
        field.style.borderColor = '#48c78e';
        field.style.backgroundColor = '#f0fff4';
    }
    
    setTimeout(() => {
        field.style.borderColor = '#e9ecef';
        field.style.backgroundColor = 'white';
    }, 2000);
}

document.addEventListener('DOMContentLoaded', function() {
    const aboutText = document.querySelector('.about-text p');
    if (aboutText) {
        const text = aboutText.textContent;
        aboutText.textContent = '';
        aboutText.style.borderRight = '2px solid #667eea';
        
        let i = 0;
        const typeWriter = () => {
            if (i < text.length) {
                aboutText.textContent += text.charAt(i);
                i++;
                setTimeout(typeWriter, 30);
            } else {
                aboutText.style.borderRight = 'none';
            }
        };
        
        const aboutSection = document.getElementById('about');
        const typeObserver = new IntersectionObserver(function(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    setTimeout(typeWriter, 500);
                    typeObserver.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });
        
        typeObserver.observe(aboutSection);
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const buttons = document.querySelectorAll('button, .home-icon');
    
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.classList.add('ripple');
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
});

const style = document.createElement('style');
style.textContent = `
    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.6);
        transform: scale(0);
        animation: ripple-animation 0.6s linear;
        pointer-events: none;
    }
    
    @keyframes ripple-animation {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
    
    button, .home-icon {
        position: relative;
        overflow: hidden;
    }
    
    .nav-menu a.active {
        background-color: rgba(255, 255, 255, 0.2);
        font-weight: 600;
    }
`;
document.head.appendChild(style);