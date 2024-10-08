package com.example.osbb.controller.constants;

public class ApiPaths {
    public static final String URL_SERVER = "http://localhost:9000";
    public static final String ACTUATOR = "/actuator";
    // authentication --------
    public static final String AUTH = "/auth";
    public static final String USER = "/user";
    public static final String REGISTRATION = "/registration";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String ACTIVATE = "/activate";
    public static final String REFRESH = "/refresh";
    public static final String CHECK = "/check";
    public static final String LINK = "/{link}";
    // owner -----------------
    public static final String PASSPORT = "/passport";
    public static final String VEHICLE = "/vehicle";
    public static final String PHOTO = "/photo";
    public static final String OWNER = "/owner";
    public static final String PLACE_WORK = "/place_work";
    // ownership -------------
    public static final String OWNERSHIP = "/ownership";
    public static final String ADDRESS = "/address";
    public static final String APARTMENT = "/apartment";
    public static final String ID = "/id";
    public static final String REPORT = "/report";
    public static final String CHARACTERISTICS = "/characteristics";

    // payment ---------------
    public static final String PRINT = "/print";
    public static final String BALANCE = "/balance";
    public static final String DETAILS = "/details";
    public static final String DEBT = "/debt";
    public static final String PAYMENT = "/payment";
    public static final String BILL = "/bill";
    public static final String NEW_BILL = "/new_bill";
    public static final String DATE = "/date";
    public static final String RATE = "/rate";

    // polls ------------------------
    public static final String SURVEYS = "/surveys";

    public static final String SELECT = "/select";
    public static final String TITLE = "/title";
    public static final String FULL_NAME = "/full_name";
    public static final String QUESTION = "/question";
    public static final String DATE_DISPATCH = "/date_dispatch";
    public static final String DATE_RECEIVING = "/date_receiving";
    public static final String RESULT = "/result";

    // other ------------------------------
    public static final String RECORD = "/record";
    public static final String REGISTRY = "/registry";
    public static final String QUERIES = "/queries";
    public static final String HEAT_SUPPLY = "/heat_supply";

    // --------------- summa ------------------
    public static final String SUMMA = "/summa";
    public static final String SUMMA_AREA_ROOMS = "/summa_area_rooms";
    public static final String SUMMA_AREA_APARTMENT = "/summa_area_apartment";
    public static final String SUMMA_AREA_LIVING_APARTMENT = "/summa_area_living_apartment";
    public static final String SUMMA_AREA_NON_RESIDENTIAL_ROOM = "/summa_area_non_residential_room";

    // ------------- count ------------------------
    public static final String COUNT = "/count";
    public static final String COUNT_ROOMS = "/count_rooms";
    public static final String COUNT_APARTMENT = "/count_apartment";
    public static final String COUNT_NON_RESIDENTIAL_ROOM = "/count_non_residential_room";

    // --------------------------------------------
    public static final String ALL = "/all";
    public static final String ALL_IN_ONE = "/all_in_one";
    public static final String REGISTRATION_NUMBER = "/registration_number";
    public static final String SS = "/**";

    // params {} -----------------------
    public static final String PARAM_ID = "/{id}";
    public static final String PARAM_STREET = "/{street}";
    public static final String PARAM_HOUSE = "/{house}";
    public static final String PARAM_TITLE = "/{title}";
    public static final String PARAM_QUESTION = "/{question}";
    public static final String PARAM_FULL_NAME = "/{fullName}";
    public static final String PARAM_APARTMENT = "/{apartment}";
    public static final String PARAM_DATE_DISPATCH = "/{dateDispatch}";
    public static final String PARAM_DATE_RECEIVING = "/{dateReceiving}";
    public static final String PARAM_OWNER_ID = "/{ownerId}";
    public static final String PARAM_OWNERSHIP_ID = "/{ownershipId}";
    public static final String PARAM_BILL = "/{bill}";
    public static final String PARAM_FROM = "/{from}";
    public static final String PARAM_TO = "/{to}";

}
