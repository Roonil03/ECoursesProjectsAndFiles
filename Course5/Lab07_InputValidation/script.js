function initPage() {
    console.log("Page loaded!");
}

function showProductImage() {
    const productImageSection = document.getElementById("productImageSection");
    const productImage = document.getElementById("productImage");
    const productCategory = document.getElementById("productCategory").value;
    if (productCategory === "phone") {
        productImage.src = "https://via.placeholder.com/300x200?text=Phone";
    } else if (productCategory === "laptop") {
        productImage.src = "https://via.placeholder.com/300x200?text=Laptop";
    } else if (productCategory === "camera") {
        productImage.src = "https://via.placeholder.com/300x200?text=Camera";
    } else if (productCategory === "headphones") {
        productImage.src = "https://via.placeholder.com/300x200?text=Headphones";
    } else if (productCategory === "watch") {
        productImage.src = "https://via.placeholder.com/300x200?text=Smart+Watch";
    }

    productImageSection.style.display = "block";
}

function submitForm() {
    let isValid = true;

    const productName = document.getElementById("productName").value;
    const productCategory = document.getElementById("productCategory").value;
    const productID = document.getElementById("productID").value;
    const serialNumber = document.getElementById("serialNumber").value;
    const warranty = document.getElementById("warranty").value;
    const price = document.getElementById("price").value;
    const vendor = document.getElementById("vendor").value;
    const email = document.getElementById("email").value;
    const phoneNumber = document.getElementById("phoneNumber").value;

    clearErrors();

    // Product Name validation (non-empty)
    if (!productName) {
        displayError("productNameError", "Product name is required.");
        isValid = false;
    }

    // Product Category validation (non-empty)
    if (!productCategory) {
        displayError("productCategoryError", "Please select a product category.");
        isValid = false;
    }

    // Product ID validation (non-empty)
    if (!productID) {
        displayError("productIDError", "Product ID is required.");
        isValid = false;
    }


    // Serial Number (non-empty)
    if (!serialNumber) {
        displayError("serialNumberError", "Serial Number is required.");
        isValid = false;
    }

    // Warranty validation (1-5 years)
    if (!warranty || warranty < 1 || warranty > 5) {
        displayError("warrantyError", "Warranty must be between 1 and 5 years.");
        isValid = false;
    }

    // Price validation (positive number)
    if (!price || price <= 0) {
        displayError("priceError", "Price must be a positive number.");
        isValid = false;
    }

    // Vendor validation (non-empty)
    if (!vendor) {
        displayError("vendorError", "Vendor name is required.");
        isValid = false;
    }

    // Email validation (valid email format)
    if (!email || !email.includes("@")) {
        displayError("emailError", "Please enter a valid email.");
        isValid = false;
    }

    // Phone Number validation (numeric, 10 digits)
    if (!phoneNumber || phoneNumber.length !== 10) {
        displayError("phoneNumberError", "Phone number must be 10 digits.");
        isValid = false;
    }

    if (isValid) {
        document.getElementById("productForm").style.display = "none";
        document.getElementById("thanksMessage").style.display = "block";
    }
}

function clearErrors() {
    const errors = document.querySelectorAll(".error-message");
    errors.forEach(error => error.innerHTML = "");
}

function displayError(elementId, message) {
    document.getElementById(elementId).innerHTML = message;
}
