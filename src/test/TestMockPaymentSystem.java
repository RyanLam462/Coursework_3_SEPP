package test;

import external.MockPaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMockPaymentSystem {

    private MockPaymentSystem paymentSystem;

    /* runs before each test, each test gets a fresh
    MockPaymentSystem object */
    @BeforeEach
    void setUp() {
        paymentSystem = new MockPaymentSystem();
    }

// ============ processPayment tests ============

    // Tests case where all inputs are valid returns true
    @Test
    void processPaymentValidInputsReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertTrue(result, "processPayment should return true for valid inputs");
    }

//--------Testing null string cases--------

    // Testing case where studentEmail is null returns false
    @Test
    void processPaymentNullStudentEmailReturnsFalse() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", null,
                123456789, "ep@company.com",
                50.0);
        assertFalse(result, "processPayment should return false for null studentEmail input");
    }

    // Testing case where eventTitle is null returns false
    @Test
    void processPaymentNullEventTitleReturnsFalse() {
        boolean result = paymentSystem.processPayment(2,
                null, "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertFalse(result, "processPayment should return false for null eventTitle input");
    }

    // Testing case where epEmail is null returns false
    @Test
    void processPaymentNullEpEmailReturnsFalse() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, null,
                50.0);
        assertFalse(result, "processPayment should return false for null epEmail input");
    }

//--------Testing invalid numeric values-------

    // Testing case where numTicket is 0 returns false
    @Test
    void processPaymentZeroNumTicketsReturnsFalse() {
        boolean result = paymentSystem.processPayment(0,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertFalse(result, "processPayment should return false for zero numTickets");
    }

    // Testing case where numTicket is negative returns false
    @Test
    void processPaymentNegativeNumTicketsReturnsFalse() {
        boolean result = paymentSystem.processPayment(-1,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertFalse(result, "processPayment should return false for negative numTickets");
    }

    // Testing case where transactionAmount is 0 returns false
    @Test
    void processPaymentZeroTransactionAmountReturnsFalse() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0);
        assertFalse(result, "processPayment should return false for zero transactionAmount");
    }

    // Testing case where transactionAmount is negative returns false
    @Test
    void processPaymentNegativeTransactionAmountReturnsFalse() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                -1);
        assertFalse(result, "processPayment should return false for negative transactionAmount");
    }

//--------Testing boundary valid inputs-------

    // Testing boundary case where numTickets = 1 returns true
    @Test
    void processPaymentNumTicketsBoundaryReturnsTrue() {
        boolean result = paymentSystem.processPayment(1,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertTrue(result, "processPayment should return true for numTickets = 1");
    }

    // Testing boundary case where transactionAmount = 0.01 returns true
    @Test
    void processPaymentTransactionAmountBoundaryReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0.01);
        assertTrue(result, "processPayment should return true for transactionAmount = 0.01");
    }

//--------Other misc valid cases-------

    // Testing case where numTickets is large returns true
    @Test
    void processPaymentLargeNumTicketsReturnsTrue() {
        boolean result = paymentSystem.processPayment(10000,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0.01);
        assertTrue(result, "processPayment should return true for large numTickets");
    }

    // Testing case where TransactionAmount is large returns true
    @Test
    void processPaymentLargeTransactionAmountReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                10000000.0);
        assertTrue(result, "processPayment should return true for large transactionAmount");
    }

    // Testing case where studentEmail is empty string returns true
    @Test
    void processPaymentEmptyStudentEmailReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "",
                123456789, "ep@company.com",
                50.0);
        assertTrue(result, "processPayment should return true for empty studentEmail");
    }

    // Testing case where epEmail is empty string returns true
    @Test
    void processPaymentEmptyEpEmailReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "Concert", "student@ed.ac.uk",
                123456789, "",
                50.0);
        assertTrue(result, "processPayment should return true for empty epEmail");
    }

    // Testing case where eventTitle is empty string returns true
    @Test
    void processPaymentEmptyEventTitleReturnsTrue() {
        boolean result = paymentSystem.processPayment(2,
                "", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0);
        assertTrue(result, "processPayment should return true for empty eventTitle");
    }


// ============ processRefund tests ============

    // Tests case where all inputs are valid returns true
    @Test
    void processRefundValidInputsReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertTrue(result, "processRefund should return true for valid inputs");
    }

//--------Testing null string cases--------

    // Testing case where studentEmail is null returns false
    @Test
    void processRefundNullStudentEmailReturnsFalse() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", null,
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertFalse(result, "processRefund should return false for null studentEmail input");
    }

    // Testing case where eventTitle is null returns false
    @Test
    void processRefundNullEventTitleReturnsFalse() {
        boolean result = paymentSystem.processRefund(2,
                null, "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertFalse(result, "processRefund should return false for null eventTitle input");
    }

    // Testing case where epEmail is null returns false
    @Test
    void processRefundNullEpEmailReturnsFalse() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, null,
                50.0, "Sorry");
        assertFalse(result, "processRefund should return false for null epEmail input");
    }

//--------Testing invalid numeric values-------

    // Testing case where numTicket is 0 returns false
    @Test
    void processRefundZeroNumTicketsReturnsFalse() {
        boolean result = paymentSystem.processRefund(0,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertFalse(result, "processRefund should return false for zero numTickets");
    }

    // Testing case where numTicket is negative returns false
    @Test
    void processRefundNegativeNumTicketsReturnsFalse() {
        boolean result = paymentSystem.processRefund(-1,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertFalse(result, "processRefund should return false for negative numTickets");
    }

    // Testing case where transactionAmount is 0 returns false
    @Test
    void processRefundZeroTransactionAmountReturnsFalse() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0, "Sorry");
        assertFalse(result, "processRefund should return false for zero transactionAmount");
    }

    // Testing case where transactionAmount is negative returns false
    @Test
    void processRefundNegativeTransactionAmountReturnsFalse() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                -1, "Sorry");
        assertFalse(result, "processRefund should return false for negative transactionAmount");
    }

//--------Testing boundary valid inputs-------

    // Testing boundary case where numTickets = 1 returns true
    @Test
    void processRefundNumTicketsBoundaryReturnsTrue() {
        boolean result = paymentSystem.processRefund(1,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertTrue(result, "processRefund should return true for numTickets = 1");
    }

    // Testing boundary case where transactionAmount = 0.01 returns true
    @Test
    void processRefundTransactionAmountBoundaryReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0.01, "Sorry");
        assertTrue(result, "processRefund should return true for transactionAmount = 0.01");
    }

//--------Other misc valid cases-------

    // Testing case where numTickets is large returns true
    @Test
    void processRefundLargeNumTicketsReturnsTrue() {
        boolean result = paymentSystem.processRefund(10000,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                0.01, "Sorry");
        assertTrue(result, "processRefund should return true for large numTickets");
    }

    // Testing case where TransactionAmount is large returns true
    @Test
    void processRefundLargeTransactionAmountReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                10000000.0, "Sorry");
        assertTrue(result, "processRefund should return true for large transactionAmount");
    }

    // Testing case where studentEmail is empty string returns true
    @Test
    void processRefundEmptyStudentEmailReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertTrue(result, "processRefund should return true for empty studentEmail");
    }

    // Testing case where epEmail is empty string returns true
    @Test
    void processRefundEmptyEpEmailReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "",
                50.0, "Sorry");
        assertTrue(result, "processRefund should return true for empty epEmail");
    }

    // Testing case where eventTitle is empty string returns true
    @Test
    void processRefundEmptyEventTitleReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "Sorry");
        assertTrue(result, "processRefund should return true for empty eventTitle");
    }

//--------processRefund specific organiserMsg tests-------

    // Testing case where organiserMsg is null returns true
    @Test
    void processRefundNullOrganiserMsgReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, null);
        assertTrue(result, "processRefund should return true when organiserMsg is null");
    }

    // Testing case where organiserMsg is empty string returns true
    @Test
    void processRefundEmptyOrganiserMsgReturnsTrue() {
        boolean result = paymentSystem.processRefund(2,
                "Concert", "student@ed.ac.uk",
                123456789, "ep@company.com",
                50.0, "");
        assertTrue(result, "processRefund should return true when organiserMsg is empty string");
    }
}
