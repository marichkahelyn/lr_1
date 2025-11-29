package insurance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsuranceManagerTest {

    private InsuranceManager manager;

    @BeforeEach
    void setUp() {
        manager = new InsuranceManager();
    }

    // ---------------------------------------------------------
    // 1. Тест успішного парсингу AUTO
    // ---------------------------------------------------------
    @Test
    void testParseAutoPolicySuccess() throws Exception {
        String line = "auto,Моя Mazda,150000,0.3,12,Sedan,0,true";

        InsurancePolicy policy = manager.parsePolicyFromLine(line, 100);

        assertInstanceOf(AutoInsurance.class, policy);
        assertEquals("Моя Mazda", policy.getName());
        assertEquals(150000, policy.getObligation());
        assertEquals(0.3, policy.getRisk());
        assertEquals(12, policy.getDuration());
        assertEquals(100, policy.getId());
    }

    // ---------------------------------------------------------
    // 2. Тест успішного парсингу LIFE
    // ---------------------------------------------------------
    @Test
    void testParseLifePolicySuccess() throws Exception {
        String line = "life,На майбутнє,1000000,0.15,30,30,1000000,good";

        InsurancePolicy policy = manager.parsePolicyFromLine(line, 101);

        assertInstanceOf(LifeInsurance.class, policy);
        assertEquals("На майбутнє", policy.getName());
        assertEquals(0.15, policy.getRisk());
        assertEquals(101, policy.getId());
    }

    // ---------------------------------------------------------
    // 3. Невідомий тип → має бути Exception
    // ---------------------------------------------------------
    @Test
    void testParseUnknownType() {
        String line = "boat,Титанік,1000,0.9,1,Ocean,1,false";

        Exception ex = assertThrows(Exception.class, () ->
                manager.parsePolicyFromLine(line, 102)
        );

        assertTrue(ex.getMessage().contains("Невідомий тип"));
    }

    // ---------------------------------------------------------
    // 4. Недостатньо даних → Exception
    // ---------------------------------------------------------
    @Test
    void testParseNotEnoughData() {
        String line = "auto,Тесла,200000,0.5";

        Exception ex = assertThrows(Exception.class, () ->
                manager.parsePolicyFromLine(line, 103)
        );

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }

    // ---------------------------------------------------------
    // 5. Перевірка правильності конвертації у файл (AUTO)
    // ---------------------------------------------------------
    @Test
    void testConvertAutoToFileFormat() {
        AutoInsurance a = new AutoInsurance(10, "Mazda", 100000, 0.2, 12,
                "Sedan", 1, true);

        String line = manager.convertPolicyToLine(a);

        assertEquals(
                "auto,Mazda,100000.0,0.2,12,Sedan,1,true",
                line
        );
    }

    // ---------------------------------------------------------
    // 6. Перевірка формату property
    // ---------------------------------------------------------
    @Test
    void testConvertPropertyToFileFormat() {
        PropertyInsurance pr = new PropertyInsurance(
                9, "Home", 500000, 0.1, 24,
                "House", "Medium", true
        );

        String line = manager.convertPolicyToLine(pr);

        assertEquals(
                "property,Home,500000.0,0.1,24,House,Medium,true",
                line
        );
    }

    // ---------------------------------------------------------
    // 7. Тест на видалення полісу
    // ---------------------------------------------------------
    @Test
    void testRemovePolicy() {
        Derivative d = new Derivative();
        d.addPolicy(new AutoInsurance(1, "A", 1,1,1,"s",0,false));
        d.addPolicy(new AutoInsurance(2, "B", 1,1,1,"s",0,false));

        assertTrue(manager.removePolicyById(1, d));
        assertEquals(1, d.getPolicies().size());

        assertFalse(manager.removePolicyById(99, d));
    }

    // ---------------------------------------------------------
    // 8. Тест saveToFile (перевірка що файл створюється)
    // ---------------------------------------------------------
    @Test
    void testSaveToFileCreatesFile() {
        try {
            Derivative d = new Derivative();
            d.addPolicy(new AutoInsurance(1, "Mazda", 100000, 0.2, 12, "Sedan", 0, true));

            String filename = "test_output.txt";

            manager.saveToFile(filename, d);

            java.io.File f = new java.io.File(filename);

            assertTrue(f.exists());
            assertTrue(f.length() > 0);

            f.delete();
        } catch (Exception e) {
            fail("saveToFile unexpected exception: " + e.getMessage());
        }
    }
    // ---------------------------------------------------------
// 9. Створення MedicalInsurance через фабричний метод
// ---------------------------------------------------------
    @Test
    void testCreateMedicalPolicy() {
        MedicalInsurance m = (MedicalInsurance) manager.createMedicalPolicy(
                5, "Health", 20000, 0.1, 12, 60, "Full", "Premium");

        assertEquals(5, m.getId());
        assertEquals("Health", m.getName());
        assertEquals(60, m.getAgeLimit());
        assertEquals("Full", m.getCoverage());
    }

    // ---------------------------------------------------------
// 10. Створення TravelInsurance
// ---------------------------------------------------------
    @Test
    void testCreateTravelPolicy() {
        TravelInsurance t = (TravelInsurance) manager.createTravelPolicy(
                6, "Trip", 15000, 0.3, 30, "Spain", 14, 0.05);

        assertEquals("Spain", t.getCountry());
        assertEquals(14, t.getTripDays());
        assertEquals(0.05, t.getAccidentRisk());
    }

    // ---------------------------------------------------------
// 11. Створення AgroInsurance
// ---------------------------------------------------------
    @Test
    void testCreateAgroPolicy() {
        AgroInsurance a = (AgroInsurance) manager.createAgroPolicy(
                7, "Wheat", 50000, 0.2, 12, "Wheat", 5.0, 0.03);

        assertEquals(5.0, a.getArea());
        assertEquals(0.03, a.getWeatherRisk());
    }

    // ---------------------------------------------------------
// 12. Погані числові значення
// ---------------------------------------------------------
    @Test
    void testParseInvalidNumber() {
        String line = "auto,Car,abc,0.2,12,Sedan,1,true";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 10));

        assertTrue(ex.getMessage().contains("Некоректні числові"));
    }

    // ---------------------------------------------------------
// 13. Парсинг travel з неправильним числом днів
// ---------------------------------------------------------
    @Test
    void testParseTravelInvalidDays() {
        String line = "travel,Trip,10000,0.1,10,Spain,xxx,0.03";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 11));

        assertTrue(ex.getMessage().contains("Некоректні"));
    }

    // ---------------------------------------------------------
// 14. Конвертація MedicalInsurance у рядок
// ---------------------------------------------------------
    @Test
    void testConvertMedicalToFileFormat() {
        MedicalInsurance m = new MedicalInsurance(
                3, "Health", 20000, 0.1, 12, 60, "Full", "Premium");

        String line = manager.convertPolicyToLine(m);

        assertEquals(
                "medical,Health,20000.0,0.1,12,60,Full,Premium",
                line
        );
    }

    // ---------------------------------------------------------
// 15. Конвертація AgroInsurance
// ---------------------------------------------------------
    @Test
    void testConvertAgroToFileFormat() {
        AgroInsurance a = new AgroInsurance(
                8, "Farm", 60000, 0.05, 12, "Corn", 10.5, 0.03);

        String line = manager.convertPolicyToLine(a);

        assertEquals(
                "agro,Farm,60000.0,0.05,12,Corn,10.5,0.03",
                line
        );
    }

    // ---------------------------------------------------------
// 16. Конвертація TravelInsurance
// ---------------------------------------------------------
    @Test
    void testConvertTravelToFileFormat() {
        TravelInsurance t = new TravelInsurance(
                9, "Trip", 15000, 0.04, 30, "Italy", 14, 0.02);

        String line = manager.convertPolicyToLine(t);

        assertEquals(
                "travel,Trip,15000.0,0.04,30,Italy,14,0.02",
                line
        );
    }

    // ---------------------------------------------------------
// 17. Звіт: перевірка що System.out щось виводить
// ---------------------------------------------------------
    @Test
    void testGenerateReportOutput() {
        Derivative d = new Derivative();
        d.addPolicy(new AutoInsurance(1, "A", 10000, 0.2, 12, "Sedan", 0, false));

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        manager.generateReport(d);

        String output = out.toString();
        assertTrue(output.contains("Звіт по Деривативу"));
        assertTrue(output.contains("A"));
        assertTrue(output.contains("Загальна вартість"));
    }

    // ---------------------------------------------------------
// 18. Помилка запису у файл (наприклад, у папку без прав)
// ---------------------------------------------------------
    @Test
    void testSaveToFileIOException() {
        Derivative d = new Derivative();
        d.addPolicy(new AutoInsurance(1, "A", 10000, 0.2, 12, "Sedan", 0, false));

        // Неможливий шлях → гарантована помилка
        String filename = "Z:/__does_not_exist__/file.txt";

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        manager.saveToFile(filename, d);

        String output = out.toString();
        assertTrue(output.contains("Помилка запису"));
    }
    // ---------------------------------------------------------
// 19. Порожній рядок → Exception
// ---------------------------------------------------------
    @Test
    void testParseEmptyLine() {
        String line = "";
        Exception ex = assertThrows(Exception.class, () ->
                manager.parsePolicyFromLine(line, 50)
        );
        assertTrue(ex.getMessage().contains("Недостатньо"));
    }

    // ---------------------------------------------------------
// 20. Параметри лише з пробілами → Exception
// ---------------------------------------------------------
    @Test
    void testParseSpacesOnly() {
        String line = "   ,   ,   ";
        Exception ex = assertThrows(Exception.class, () ->
                manager.parsePolicyFromLine(line, 51)
        );
        assertTrue(ex.getMessage().contains("Недостатньо"));
    }


    // ---------------------------------------------------------
// 24. Wrong format: travel accidentRisk не double
// ---------------------------------------------------------
    @Test
    void testParseTravelInvalidAccidentRisk() {
        String line = "travel,Trip,10000,0.1,10,Spain,5,abc";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 55)
        );

        assertTrue(ex.getMessage().contains("Некоректні"));
    }

    // ---------------------------------------------------------
// 25. LifeInsurance з некоректним віком
// ---------------------------------------------------------
    @Test
    void testParseLifeInvalidAge() {
        String line = "life,Life,10000,0.1,12,notAge,50000,good";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 56)
        );

        assertTrue(ex.getMessage().contains("Некоректні"));
    }


    // ---------------------------------------------------------
// 28. Пробіли навколо чисел — має працювати
// ---------------------------------------------------------
    @Test
    void testParseTrimValues() throws Exception {
        String line = "auto , Mazda , 100000 , 0.2 , 12 , Sedan , 0 , true";

        InsurancePolicy p = manager.parsePolicyFromLine(line, 59);

        assertInstanceOf(AutoInsurance.class, p);
        assertEquals("Mazda", p.getName());
        assertEquals(100000, p.getObligation());
    }



    // ---------------------------------------------------------
// 30. Тест що convertPolicyToLine не повертає пустий рядок
// ---------------------------------------------------------
    @Test
    void testConvertUnknownPolicyType() {
        InsurancePolicy p = new InsurancePolicy(1, "Test", 100, 0.1, 12) {
            @Override
            public double calculatePremium() { return 0; }
        };

        String line = manager.convertPolicyToLine(p);

        assertNotNull(line);
        assertEquals("", line); // за твоїм кодом unknown = пустий
    }
    @Test
    void testConvertBasePolicyReturnsEmptyString() {
        InsurancePolicy p = new InsurancePolicy(99, "X", 10, 0.1, 5) {
            @Override public double calculatePremium() { return 0; }
        };

        String line = manager.convertPolicyToLine(p);

        assertEquals("", line);
    }
    @Test
    void testParsePropertyPolicySuccess() throws Exception {
        String line = "property,House,300000,0.12,24,Cottage,High,true";

        InsurancePolicy p = manager.parsePolicyFromLine(line, 200);

        assertInstanceOf(PropertyInsurance.class, p);
        assertEquals("House", p.getName());
        assertEquals(300000, p.getObligation());
        assertEquals(0.12, p.getRisk());
        assertEquals(24, p.getDuration());
    }
    @Test
    void testParseMedicalPolicySuccess() throws Exception {
        String line = "medical,Health,20000,0.1,12,60,Full,Premium";

        InsurancePolicy p = manager.parsePolicyFromLine(line, 201);

        assertInstanceOf(MedicalInsurance.class, p);
        assertEquals("Health", p.getName());
        assertEquals(60, ((MedicalInsurance)p).getAgeLimit());
    }
    @Test
    void testParseAgroPolicySuccess() throws Exception {
        String line = "agro,Wheat,50000,0.2,12,Wheat,10.5,0.03";

        InsurancePolicy p = manager.parsePolicyFromLine(line, 202);

        assertInstanceOf(AgroInsurance.class, p);
        assertEquals(10.5, ((AgroInsurance)p).getArea());
    }
    @Test
    void testParseTravelPolicySuccess() throws Exception {
        String line = "travel,Trip,15000,0.3,30,Italy,14,0.05";

        InsurancePolicy p = manager.parsePolicyFromLine(line, 203);

        assertInstanceOf(TravelInsurance.class, p);
        assertEquals("Italy", ((TravelInsurance)p).getCountry());
        assertEquals(14, ((TravelInsurance)p).getTripDays());
    }
    @Test
    void testSaveToFileEmptyDerivative() {
        Derivative d = new Derivative();

        String filename = "empty_test.txt";

        manager.saveToFile(filename, d);

        java.io.File f = new java.io.File(filename);
        assertTrue(f.exists());
        assertEquals(0, f.length()); // файл має бути порожній

        f.delete();
    }


    @Test
    void testParsePropertyNotEnoughData() {
        String line = "property,Home,1000,0.1,12,House";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 70));

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }
    @Test
    void testParseMedicalNotEnoughData() {
        String line = "medical,Health,10000,0.2,12,60";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 71));

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }
    @Test
    void testParseTravelNotEnoughData() {
        String line = "travel,Trip,20000,0.3,10,Spain";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 72));

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }
    @Test
    void testParseLifeNotEnoughData() {
        String line = "life,Future,50000,0.4,20,30";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 73));

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }
    @Test
    void testParseAgroNotEnoughData() {
        String line = "agro,Wheat,30000,0.3,12,Corn";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 74));

        assertTrue(ex.getMessage().contains("Недостатньо"));
    }
    @Test
    void testParseInvalidNumberProperty() {
        String line = "property,Home,abc,0.1,12,House,Low,true";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 75));

        assertTrue(ex.getMessage().contains("Некоректні"));
    }
    @Test
    void testCreateLifePolicy() {
        LifeInsurance l = (LifeInsurance) manager.createLifePolicy(
                77, "Life", 10000, 0.1, 12, 30, 50000, "good"
        );

        assertEquals(30, l.getAge());
        assertEquals(50000, l.getPayout());
    }
    @Test
    void testParseInvalidDuration() {
        String line = "auto,BMW,1000,0.1,abc,X5,2018,20000";

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                manager.parsePolicyFromLine(line, 31)
        );

        assertTrue(ex.getMessage().contains("Некоректні"));
    }


}