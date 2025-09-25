/** Клас LucasNumber представляє числа Люка.
 * Він зберігає номер елемента послідовності та його значення.
 */
public class LukeNumber  {
    /** Номер числа Люка у послідовності */
    private int index;

    /** Значення числа Люка */
    private long value;

    /** Конструктор для створення числа Люка
     * @param index номер числа у послідовності
     * @param value Значення числа Люка
     */

    public LukeNumber (int index, long value) {
        this.index = index;
        this.value = value;
    }

    /** Повертає номер числа Люка */
    public int getIndex() {
        return index;
    }

    /** Повертає значення числа Люка */
    public long getValue() {
        return value;
    }

    /** Перевіряє, чи є число Люка кубом цілого числа.
     * @return true, якщо число є кубом, інакше false
     */
    public boolean isCube() {
        //знаходимо найближчий цілий корінь кубічний
        long root = Math.round((Math.cbrt(value)));
        //перевіряємо, чи справді це куб
        return root * root * root == value;
    }

}


