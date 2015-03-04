#!/bin/bash
cd ../../
xjc -d src -p at.medevit.elexis.at.rezepte.model rsc/schema/RezeptAT.xsd

# Following errors: Verschreibungen is not initialized (do lazy initialization on getVerschreibungen)
# 					Remove namespace declaration from package-info.java, otherwise big trouble...

