package com.hercats.dev.articleprovider.controller

import com.hercats.dev.commonbase.mapper.CatalogMapper
import com.hercats.dev.commonbase.model.ArticleCatalog
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.tool.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class CatalogController(@Autowired val catalogMapper: CatalogMapper) {

    @RequestMapping(value = ["article/catalog", "article/catalog"], method = [RequestMethod.GET])
    fun getArticleCatalogs(): Message {
        val msg = Message()
        val catalogs = catalogMapper.select()
        msg ok ""
        msg.map("catalogs", catalogs)
        return msg
    }

    @RequestMapping(value = ["article/catalog", "article/catalog"], method = [RequestMethod.POST])
    fun addCatalog(catalog: ArticleCatalog): Message {
        val msg = Message()
        if (catalog.catalog be blank) {
            msg error_400 "分类不能为空"
        } else {
            if (catalogMapper.insert(catalog) == 1) {
                msg ok ""
                msg.map("catalog", catalog)
            } else {
                msg error_500 "添加新的分类出错"
            }
        }
        return msg
    }

}