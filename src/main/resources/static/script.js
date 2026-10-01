document.addEventListener("DOMContentLoaded", function () {

    // Load all properties
    loadProperties("/api/properties");

    // Search button click
    document.getElementById("searchBtn").addEventListener("click", function () {

    const city = document.getElementById("searchCity").value.trim();
    const type = document.getElementById("filterType").value;
    const maxRent = document.getElementById("filterRent").value.trim();

    let url = "/api/properties/filter?";

    if (city) url += "city=" + city + "&";
    if (type) url += "type=" + type + "&";
    if (maxRent) url += "maxRent=" + maxRent;

    console.log(url);

    loadProperties(url);
        document.getElementById("properties").scrollIntoView({ behavior: "smooth" });
    });

});


function loadProperties(url) {
    fetch(url)
    .then(response => {
        if (!response.ok) throw new Error("Failed to fetch");
        return response.json();
    })
    .then(data => {
         console.log("Received:", data);
        const propertyList = document.getElementById("propertyList");

        if (data.length === 0) {
            propertyList.innerHTML = `
                <div class="col-12 text-center">
                    <h4 class="text-muted">No properties found.</h4>
                </div>`;
            return;
        }

        let cards = "";
        data.forEach(p => {
            cards += `
            <div class="col-lg-4 col-md-6 mb-4">
                <a href="/login.html" style="text-decoration:none; color:inherit;">
                    <div class="card shadow-sm h-100 border-0" style="cursor:pointer; transition:.3s;">
                        <img src="${p.imageName ? p.imageName : 'https://placehold.co/400x250'}"
                             class="card-img-top"
                             style="height:220px; object-fit:cover;">
                        <div class="card-body">
                            <h5 class="card-title">${p.title}</h5>
                            <p class="text-muted mb-1">
                                <i class="bi bi-geo-alt-fill"></i> ${p.city}
                            </p>
                            <p class="fw-bold text-success fs-5">₹${p.rent}/month</p>
                            <span class="badge bg-success">${p.type}</span>
                        </div>
                    </div>
                </a>
            </div>`;
        });

        propertyList.innerHTML = cards;
    })
    .catch(error => {
        console.error(error);
        document.getElementById("propertyList").innerHTML = `
            <div class="col-12 text-center text-danger">
                Unable to load properties.
            </div>`;
    });
}

 // Clear button
document.getElementById("clearBtn").addEventListener("click", function() {
    document.getElementById("searchCity").value = "";
    document.getElementById("filterType").value = "";
    document.getElementById("filterRent").value = "";
    
    loadProperties("/api/properties");
    document.getElementById("properties").scrollIntoView({ behavior: "smooth" });
});
