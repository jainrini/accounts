create function accounts.data_audit()
    returns trigger
    language plpgsql
as  $function$
DECLARE
v_time TIMESTAMP WITH TIME ZONE;
BEGIN

  v_time := current_timestamp;

  IF TG_OP = 'INSERT' THEN
    NEW.created_dtime := v_time;
ELSE
    NEW.created_dtime := OLD.created_dtime;
END IF;
NEW.modified_dtime := v_time;
RETURN NEW;
END
$function$;

CREATE TRIGGER set_updated_at
    BEFORE INSERT OR  UPDATE ON accounts.account
                          FOR EACH ROW
                          EXECUTE FUNCTION accounts.data_audit();

CREATE TRIGGER set_updated_at
    BEFORE INSERT OR  UPDATE ON accounts.balance
    FOR EACH ROW
EXECUTE FUNCTION accounts.data_audit();


CREATE TRIGGER set_updated_at
    BEFORE INSERT OR  UPDATE ON accounts.account_transaction
    FOR EACH ROW
EXECUTE FUNCTION accounts.data_audit();