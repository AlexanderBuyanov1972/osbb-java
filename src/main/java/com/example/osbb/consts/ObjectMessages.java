package com.example.osbb.consts;

public class ObjectMessages {
//    public static final String LIST_EMPTY = "List is empty";

    public static String listEmpty() {
        return "List is empty";
    }

    public static String deletionCompleted() {
        return "Deletion completed successfully";
    }

    public static String withSuchIdNotExists(String word) {
        return word + " with such id not exists";
    }

    public static String withSuchIdAlreadyExists(String word) {
        return word + " with such id already exists";
    }

    public static String noObjectCreated(String word) {
        return "No " + word + " created";
    }

    public static String noObjectUpdated(String word) {
        return "No " + word + " updated";
    }

    // ------------- address -------------------
    public static String addressWithSuchAddressAlreadyExists() {
        return "Address with such street, with such house and such apartment already exists";
    }

    public static String addressWithSuchAddressNoExists() {
        return "Address with such street, with such house and such apartment not exists";
    }

    // -------------------- ownership ---------------------
    public static String ownershipWithSuchNameRoomNotExists() {
        return "Ownership with such name room not exists";
    }

    // ---------------- password --------------------

    public static String passwordWithSuchRegistrationNumberCardPayerTaxesNotExists(){
        return "Password with such registration number card payer taxes not exists";
    }

    public static String passwordWithSuchRegistrationNumberCardPayerTaxesAlreadyExists(){
        return "Password with such registration number card payer taxes already exists";
    }

    // -------------------- user ---------------

    public static String userWithSuchUsernameAlreadyExists(){
        return "User with such username already exists";
    }

    public static String userWithSuchUsernameNotExists(){
        return "User with such username not exists";
    }

    // ----------- role -----------

    public static String roleWithSuchNameAlreadyExists(){
        return "Role with such name already exists";
    }

    public static String roleWithSuchNameNotExists(){
        return "Role with such name not exists";
    }

}
