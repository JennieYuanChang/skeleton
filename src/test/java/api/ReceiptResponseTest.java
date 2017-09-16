package api;


import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;
import java.math.BigDecimal;
import java.sql.Time;
import javax.validation.Validator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import generated.tables.records.ReceiptsRecord;

public class ReceiptResponseTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        Time t = new Time(1,1,1);
        BigDecimal bd = new BigDecimal(33.44);
        ReceiptsRecord dbRecord = new ReceiptsRecord(1,t,"123",bd,1);
        ReceiptResponse r = new ReceiptResponse(dbRecord);
        assertThat(validator.validate(r), empty());
    }

}