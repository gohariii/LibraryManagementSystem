package library;

/**
 * [Abstract Class: User]
 * الكلاس ده هو الـ "Base" أو الـ "Parent" لكل أنواع المستخدمين في السيستم.
 * تم تعريفه كـ abstract عشان نطبق مبدأ الـ Generalization؛ 
 * بحيث نجمع الصفات المشتركة (ID, Name, Email) في مكان واحد بدل ما نكررها.
 */
public abstract class User {

    // ====== Attributes (Protected for Inheritance) ======
    // استخدمنا الـ private لضمان الـ Encapsulation (تغليف البيانات)
    private int id;
    private String name;
    private String email;
    private String password;

    // ====== Constructor ======
    // الكونستركتور ده بيتم استدعاؤه من الكلاسات الوارثة (Admin/Member) عن طريق super
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ====== Getters & Setters ======
    // بنستخدمهم عشان نوصل للمتغيرات الـ private بطريقة آمنة
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // لاحظ: مفيش Getter للباسورد لزيادة الأمان (Security Best Practice)
    public void setPassword(String password) {
        this.password = password;
    }

    // ====== Methods ======
    
    /**
     * ميثود للتحقق من بيانات الدخول بشكل مبدئي.
     */
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    /**
     * [Abstract Method]
     * دي أهم ميثود عشان الـ Polymorphism (تعدد الأشكال).
     * كل يوزر (Admin أو Member) هيقوم بعمل "Override" للميثود دي 
     * عشان يعرض المنيو الخاص بصلاحياته هو بس.
     */
    public abstract void showMenu();
}