package guru.qa.niffler.jupiter.extension.abstr;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext){
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spend.class
        ).ifPresent(
                spend ->
                        extensionContext
                                .getStore(NAMESPACE)
                                .put(
                                        extensionContext.getUniqueId(),
                                        createSpend(extensionContext,spend)
                                ));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext){
        SpendJson spendJson = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);

        removeSpend(spendJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }

    protected abstract SpendJson createSpend(ExtensionContext extensionContext, Spend spend);

    protected abstract void removeSpend(SpendJson spend);
}