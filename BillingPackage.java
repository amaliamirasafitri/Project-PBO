public class BillingPackage {

    public final String nama;
    public final String deskripsi;
    public final String[] fitur;
    public final String harga;

    public BillingPackage(String n, String d, String[] f, String h) {
        this.nama = n;
        this.deskripsi = d;
        this.fitur = f;
        this.harga = h;
    }
}