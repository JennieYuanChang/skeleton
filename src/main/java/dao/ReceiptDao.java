package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static org.jooq.impl.DSL.*;


public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public void update(int id, String tag){
        int checkId = dsl
                .select(DSL.field("id"))
                .from(DSL.table("tags"))
                .where(DSL.field("tag").eq(tag))
                        .and(DSL.field("id").eq(id))
                        .execute();

        if(checkId>0){
                    dsl
                    .deleteFrom(DSL.table("tags"))
                    .where(DSL.field("tag").eq(tag)).and(DSL.field("id").eq(id))
                    .execute();
        }else{
                    dsl
                    .insertInto(DSL.table("tags"),DSL.field("id"), DSL.field("tag"))
                    .values(id,tag)
                    .execute();
        }
    }

    public List<ReceiptsRecord> getByTag(String tag){
           return dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(
                        dsl.select(DSL.field("id"))
                        .from(DSL.table("tags"))
                        .where(DSL.field("tag").eq(tag))
                        .fetch().getValues("id")
                ))
                .fetch();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

}
