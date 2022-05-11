package controller;

import model.LoginCredential;
import model.PGConfigDelta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {
    @Mock
    Server server;

    PGConfigDelta normal = new PGConfigDelta("authentication_timeout", "60", "s", "integer");
    PGConfigDelta bad = new PGConfigDelta("array_nulls", "bad", null, "bool");

    @Before
    public void setUp() throws Exception {
        PGConfigDelta element1 = new PGConfigDelta("authentication_timeout", "60", "s", "integer");

        Mockito.when(server.executeCommand("psql postgres -c \"alter system set authentication_timeout to \'60\'\""))
                .thenReturn("ALTER SYSTEM");
        Mockito.when(server.executeCommand("psql postgres -c \"alter system set array_nulls to \'bad\'\""))
                .thenReturn("ERROR MESSAGE...");
        Mockito.doCallRealMethod().when(server).applyPGConfigDelta(normal);
        Mockito.doCallRealMethod().when(server).applyPGConfigDelta(bad);
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void applyPGConfigDelta() {
        try {
            server.applyPGConfigDelta(normal);
            server.applyPGConfigDelta(bad);
            fail();
        } catch (PGErrorException e) {
            assertEquals("ERROR MESSAGE...", e.getMessage());
        }
    }

    @Test
    public void restartPostgres() {
    }

}