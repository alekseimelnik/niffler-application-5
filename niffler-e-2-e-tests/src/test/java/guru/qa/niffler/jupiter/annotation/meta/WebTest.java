package guru.qa.niffler.jupiter.annotation.meta;


import guru.qa.niffler.jupiter.extension.abstr.AbstractSpendExtension;
import guru.qa.niffler.jupiter.extension.browser.BrowserExtension;
import guru.qa.niffler.jupiter.extension.jdbc.CategoryExtensionJdbc;
import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        CategoryExtensionJdbc.class,
        AbstractSpendExtension.class
})
public @interface WebTest {
}
