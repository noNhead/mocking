package edu.epam.izhevsk.junit;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {
    @Mock
    AccountService accountService;
    @Mock
    DepositService depositService;
    @InjectMocks
    PaymentController paymentController;

    @BeforeEach
    public void setup() throws InsufficientFundsException {
        when(accountService.isUserAuthenticated(100L)).thenReturn(Boolean.TRUE);
        when(depositService.deposit(lt(100L), anyLong())).thenReturn("Correct");
        when(depositService.deposit(geq(100L), anyLong())).thenThrow(new InsufficientFundsException());
    }

    @Test
    public void deposit50To100() throws InsufficientFundsException {
        accountService.isUserAuthenticated(100L);
        verify(accountService, times(1)).isUserAuthenticated(100L);
    }

    @Test(expected = SecurityException.class)
    public void depositToDoNotAuthentication(Long id, Long amount) {
        assertThrows(SecurityException.class, () -> paymentController.deposit(amount, id));
    }

    @Test(expected = InsufficientFundsException.class)
    public void insufficientFundsExceptionInMethodsTest() throws InsufficientFundsException {
        doThrow(new InsufficientFundsException()).when(depositService).deposit(lt(100L), any());
    }

 
}
