/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.app.web.esproduct;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.WaterfrontBaseAction;
import org.docksidestage.esflute.maihama.exbhv.ProductBhv;
import org.docksidestage.esflute.maihama.exentity.Product;
import org.lastaflute.di.core.SingletonLaContainer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class EsproductImportAction extends WaterfrontBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ProductBhv productBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index(OptionalThing<Integer> pageNumber, EsproductSearchForm form) {
        // copy data from db to elasticsearch
        SingletonLaContainer.getComponent(org.docksidestage.dbflute.exbhv.ProductBhv.class).selectCursor(cb -> {
            cb.configure(config -> {
                config.fetchSize(10);
            });
        }, entity -> {
            Product product = new Product();
            product.asDocMeta().id(entity.getProductId().toString());
            product.setDescription(entity.getProductDescription());
            product.setCategoryCode(entity.getProductCategoryCode());
            product.setHandleCode(entity.getProductHandleCode());
            product.setName(entity.getProductName());
            product.setRegisterDatetime(entity.getRegisterDatetime());
            product.setRegisterUser(entity.getRegisterUser());
            product.setRegularPrice(entity.getRegularPrice());
            product.setStatus(entity.getProductStatusCode());
            product.setUpdateDatetime(entity.getUpdateDatetime());
            product.setUpdateUser(entity.getUpdateUser());
            productBhv.insert(product);
        });
        return redirect(EsproductListAction.class);
    }

}