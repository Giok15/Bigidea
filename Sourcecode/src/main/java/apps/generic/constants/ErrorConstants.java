package apps.generic.constants;

public final class ErrorConstants {

    private ErrorConstants() {
        throw new IllegalStateException("constants class");
    }

    public static final String ERROR ="Error";
    public static final String WRONG = "Er is iets fout gegaan.";
    public static final String EMPTYFIELDS = "Niet alle velden zijn ingevuld.";
    public static final String FILENOTFOUND = "Scherm, foto of een andere file kan niet geladen worden";

    public static final String ERROR_UNKNOWN = "onbekende error";
    public static final String ERROR_FILES = "Fout bij het aanmaken/verwijderen/wijzigen van een directory of file";
    public static final String ERROR_NODESKTOPFILES = "bureablad heeft geen bestanden";
    public static final String ERROR_EMPTYFIELDS = "Niet alle velden zijn ingevuld.";
    public static final String ERROR_NOACCOUNT = "Log in om dit scherm te openen";
    public static final String ERROR_CANCELED = "U heeft de functie geannuleerd";
    public static final String ERROR_WRONG = "Er is iets fout gegaan.";
    public static final String ERROR_PASCONF = "De wachtwoorden komen niet overeen.";

    public static final String ERROR_VALIDMAIL = "Dit is geen geldige e-mailadres.";
    public static final String ERROR_PASS = "Dit is geen geldig wachtwoord. minimaal 8 karakters, 1 hoofdletter, 1 cijfer";
    public static final String ERROR_EMAILUSED = "Dit e-mailadres is al in gebruik.";
    public static final String ERROR_ACCOUNTUNKNOWN = "Deze inloggegevens zijn niet bij ons bekend";
}
