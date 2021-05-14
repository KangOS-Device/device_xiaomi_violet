#
# Copyright (C) 2019-2020 The LineageOS Project
# Copyright (C) 2018-2020 The KangOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from violet device
$(call inherit-product, device/xiaomi/violet/device.mk)

# Inherit some common KangOS stuff.
$(call inherit-product, vendor/kangos/config/common.mk)

# Bootanimation Resolution
TARGET_BOOT_ANIMATION_RES := 1080

# Gapps
USE_GAPPS := false
TARGET_INCLUDE_AOSP_REPLACEMENT := true

# Kangos Properties
KANGOS_BUILDTYPE := OFFICIAL
PRODUCT_PRODUCT_PROPERTIES += \
  ro.kangos.maintainer=kqixs \
  ro.kangos.cpu=SM6150
  
# Face unlock
TARGET_FACE_UNLOCK_SUPPORTED := true

# Device identifier. This must come after all inclusions.
PRODUCT_NAME := kangos_violet
PRODUCT_DEVICE := violet
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Redmi Note 7 Pro
PRODUCT_MANUFACTURER := Xiaomi

BUILD_FINGERPRINT := "google/redfin/redfin:11/RQ2A.210505.003/7255357:user/release-keys"

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="violet-user 9 PKQ1.181203.001 V11.0.8.0.PFHINXM release-keys" \
    PRODUCT_NAME="violet"

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi
