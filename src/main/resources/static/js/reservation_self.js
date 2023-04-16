window.onload = function() {
    // Get the reservation details entered by the user
    var reservationID = getReservationIDFromUser();
    var reservationDate = getReservationDateFromUser();
    var reservationTime = getReservationTimeFromUser();
    var badmintonCourt = getBadmintonCourtFromUser();
    var name = getNameFromUser();
    var email = getEmailFromUser();

    // Update the HTML content with the reservation details
    document.getElementById("reservation-id").innerHTML = reservationID;
    document.getElementById("reservation-date").innerHTML = reservationDate;
    document.getElementById("reservation-time").innerHTML = reservationTime;
    document.getElementById("badminton-court").innerHTML = badmintonCourt;
    document.getElementById("name").innerHTML = name;
    document.getElementById("email").innerHTML = email;
};

function getReservationIDFromUser() {
    // Replace this with your own code to get the reservation ID from the user
    return "12345";
}

function getReservationDateFromUser() {
    // Replace this with your own code to get the reservation date from the user
    return "2023-04-18";
}

function getReservationTimeFromUser() {
    // Replace this with your own code to get the reservation time from the user
    return "10:00 AM - 11:00 AM";
}

function getBadmintonCourtFromUser() {
    // Replace this with your own code to get the badminton court from the user
    return "Court 3";
}

function getNameFromUser() {
    // Replace this with your own code to get the name from the user
    return "John Doe";
}

function getEmailFromUser() {
    // Replace this with your own code to get the email from the user
    return "johndoe@example.com";
}
