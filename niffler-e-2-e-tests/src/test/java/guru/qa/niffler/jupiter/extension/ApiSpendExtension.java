package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.model.CategoryJson;

public class ApiSpendExtension extends SpendExtension{
    @Override
    protected CategoryJson createCategory(CategoryJson spend) {
        return null;
    }

    @Override
    protected void removeCategory(CategoryJson spend) {

    }
}
