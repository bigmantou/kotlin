/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.search.ideaExtensions

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.cache.CacheManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ScopeOptimizer
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.UsageSearchContext
import org.jetbrains.kotlin.fileClasses.javaFileFacadeFqName
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.search.excludeFileTypes
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtFile

class KotlinReferenceScopeOptimizer : ScopeOptimizer {
    override fun getRestrictedUseScope(element: PsiElement): SearchScope? {
        if (element is KtCallableDeclaration) {
            val file = element.parent as? KtFile ?: return null
            val useScope = element.useScope as? GlobalSearchScope ?: return null
            val packageName = file.packageFqName.takeUnless { it.isRoot } ?: return null
            val project = file.project
            val cacheManager = CacheManager.SERVICE.getInstance(project)

            val kotlinScope = GlobalSearchScope.getScopeRestrictedByFileTypes(useScope, KotlinFileType.INSTANCE)
            val nonKotlinScope = useScope.excludeFileTypes(KotlinFileType.INSTANCE) as GlobalSearchScope

            //TODO: use all components of package name?
            val shortPackageName = packageName.shortName().identifier
            val kotlinFiles = cacheManager.getVirtualFilesWithWord(shortPackageName, UsageSearchContext.IN_CODE, kotlinScope, true)

            val javaFacadeName = file.javaFileFacadeFqName.shortName().identifier
            val nonKotlinFiles = cacheManager.getVirtualFilesWithWord(javaFacadeName, UsageSearchContext.IN_CODE, nonKotlinScope, true)
            
            return GlobalSearchScope.filesScope(project, (kotlinFiles + nonKotlinFiles).asList())
        }
        return null
    }
}