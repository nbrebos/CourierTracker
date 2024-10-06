package com.example.courierapp;

public class PackageInfo {

    //Τα δεδομένα που θα εμφανίζονται στο Preview του πακέτου
    String description, address, status;
    int imageId;

    //Με την συγκρεκριμένη σειρά που θα οριστούν, θα εμφανιστούν και στο preview!!!
    public PackageInfo(String description ,String status, String address, int imageId) {
        this.description = description;
        this.address = address;
        this.status = status;
        this.imageId = imageId;
    }
}
