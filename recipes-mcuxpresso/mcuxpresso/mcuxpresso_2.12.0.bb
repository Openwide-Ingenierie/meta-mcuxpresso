SUMMARY = "MCUXpresso recipe"
DESCRIPTION = "NXP MCUXpresso for Yocto"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                     file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PROVIDES = "virtual/mcuxpresso"
BBCLASSEXTEND = "native nativesdk"
DEPENDS += "gcc-arm-none-eabi-native"

S = "${WORKDIR}"

# Clone from git repository and add custom files
SRCREV = "08209840456f5f543b3debc5603c3dbe2b40a2eb"
SRC_URI = "git://github.com/nxp-mcuxpresso/mcux-sdk.git;protocol=https;branch=main \
           "
SRC_URI:append:class-nativesdk = " \
            file://mcuxpresso.sh \
"

# Install the SDK to the sysroot-native in /usr/include/mcuxpresso
do_install() {
    # Create the include directory for the SDK
    install -d ${D}${includedir}
    mkdir -p ${D}${includedir}/mcux-sdk

    # Copy the mcuxpresso SDK root to the include directory
    cp -r ${S}/git/* ${D}${includedir}/mcux-sdk/
}

do_install:append:class-nativesdk() {
    # Create the include directory for the SDK
    install -d ${D}${base_prefix}
    mkdir -p ${D}${base_prefix}/mcux-sdk

    # Copy the mcuxpresso SDK root to the include directory
    cp -r ${S}/git/* ${D}${base_prefix}/mcux-sdk/

    # We want to add the environment-setup.d script to set the environment variables
    mkdir -p ${D}${SDKPATHNATIVE}/environment-setup.d
    install -m 644 ${WORKDIR}/mcuxpresso.sh ${D}${SDKPATHNATIVE}/environment-setup.d/mcuxpresso-setup.sh
}

# The SDK contains binaries for whatever reason
INSANE_SKIP:${PN} += "already-stripped staticdev dev-so ldflags arch"
INSANE_SKIP:${PN}-dev += "already-stripped staticdev dev-so ldflags arch"
INSANE_SKIP:${PN}-dbg += "already-stripped staticdev dev-so ldflags arch"

FILES:${PN} += "${includedir}/mcux-sdk"
FILES:${PN}:append:class-nativesdk = " ${base_prefix}/mcux-sdk ${SDKPATHNATIVE}/environment-setup.d/mcuxpresso-setup.sh"
