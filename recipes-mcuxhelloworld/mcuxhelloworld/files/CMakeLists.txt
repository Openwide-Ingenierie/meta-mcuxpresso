# CROSS COMPILER SETTING
SET(CMAKE_SYSTEM_NAME Generic)
CMAKE_MINIMUM_REQUIRED (VERSION 3.10.0)

# THE VERSION NUMBER
SET (Tutorial_VERSION_MAJOR 1)
SET (Tutorial_VERSION_MINOR 0)

# ENABLE ASM
ENABLE_LANGUAGE(ASM)

SET(CMAKE_STATIC_LIBRARY_PREFIX)
SET(CMAKE_STATIC_LIBRARY_SUFFIX)

SET(CMAKE_EXECUTABLE_LIBRARY_PREFIX)
SET(CMAKE_EXECUTABLE_LIBRARY_SUFFIX)

# CURRENT DIRECTORY
SET(ProjDirPath ${CMAKE_CURRENT_SOURCE_DIR})

if (DEFINED ENV{MCUXPRESSO_SDK})
    SET(SdkRootDirPath "$ENV{MCUXPRESSO_SDK}")
else()
    SET(SdkRootDirPath "${ProjDirPath}/../../../../..")
endif()

if (NOT DEFINED SdkRootDirPath)
    SET(SdkRootDirPath ${SdkRootDirPath})
endif()

# Set toolchain file
if (NOT DEFINED CMAKE_TOOLCHAIN_FILE)
    if (DEFINED ENV{MCUXPRESSO_CMAKE_TOOLCHAIN})
        SET(CMAKE_TOOLCHAIN_FILE "$ENV{MCUXPRESSO_CMAKE_TOOLCHAIN}")
    else()
        SET(CMAKE_TOOLCHAIN_FILE ${SdkRootDirPath}/tools/cmake_toolchain_files/armgcc.cmake)
    endif()
endif()

SET(EXECUTABLE_OUTPUT_PATH ${ProjDirPath}/${CMAKE_BUILD_TYPE})
SET(LIBRARY_OUTPUT_PATH ${ProjDirPath}/${CMAKE_BUILD_TYPE})


project(hello_world)

set(MCUX_BUILD_TYPES debug release ddr_debug ddr_release flash_debug flash_release)

set(MCUX_SDK_PROJECT_NAME boot.elf)

if (NOT DEFINED SdkRootDirPath)
    SET(SdkRootDirPath ${SdkRootDirPath})
endif()

include(${ProjDirPath}/flags.cmake)

include(${ProjDirPath}/config.cmake)

add_executable(${MCUX_SDK_PROJECT_NAME}
"${ProjDirPath}/main.c"
"${ProjDirPath}/pin_mux.c"
"${ProjDirPath}/pin_mux.h"
"${SdkRootDirPath}/boards/evkmimx8mm/board.c"
"${SdkRootDirPath}/boards/evkmimx8mm/board.h"
"${SdkRootDirPath}/boards/evkmimx8mm/clock_config.c"
"${SdkRootDirPath}/boards/evkmimx8mm/clock_config.h"
"${ProjDirPath}/fsl_iomuxc.h"
"${ProjDirPath}/empty_rsc_table.c"
)

target_include_directories(${MCUX_SDK_PROJECT_NAME} PUBLIC
    ${ProjDirPath}/
    ${SdkRootDirPath}/boards/evkmimx8mm
)

set(CMAKE_MODULE_PATH
    ${SdkRootDirPath}/utilities
    ${SdkRootDirPath}/utilities/assert
    ${SdkRootDirPath}/devices/MIMX8MM6/drivers
    ${SdkRootDirPath}/drivers/common
    ${SdkRootDirPath}/drivers/rdc
    ${SdkRootDirPath}/devices/MIMX8MM6
    ${SdkRootDirPath}/components/uart
    ${SdkRootDirPath}/components/lists
    ${SdkRootDirPath}/drivers/iuart
    ${SdkRootDirPath}/CMSIS/Core/Include
    ${SdkRootDirPath}/utilities/misc_utilities
    ${SdkRootDirPath}/utilities/debug_console_lite
)

# include modules
include(utility_debug_console_lite)

include(utility_assert_lite)

include(driver_clock)

include(driver_common)

include(driver_rdc)

include(device_CMSIS)

include(component_iuart_adapter)

include(component_lists)

include(driver_iuart)

include(device_startup)

include(CMSIS_Include_core_cm)

include(utilities_misc_utilities_MIMX8MM6)

include(device_system)


IF(NOT DEFINED TARGET_LINK_SYSTEM_LIBRARIES)
    SET(TARGET_LINK_SYSTEM_LIBRARIES "-lm -lc -lgcc -lnosys")
ENDIF()

TARGET_LINK_LIBRARIES(${MCUX_SDK_PROJECT_NAME} PRIVATE -Wl,--start-group)

target_link_libraries(${MCUX_SDK_PROJECT_NAME} PRIVATE ${TARGET_LINK_SYSTEM_LIBRARIES})

TARGET_LINK_LIBRARIES(${MCUX_SDK_PROJECT_NAME} PRIVATE -Wl,--end-group)

ADD_CUSTOM_COMMAND(TARGET ${MCUX_SDK_PROJECT_NAME} POST_BUILD COMMAND ${CMAKE_OBJCOPY}
-Obinary ${EXECUTABLE_OUTPUT_PATH}/${MCUX_SDK_PROJECT_NAME} ${EXECUTABLE_OUTPUT_PATH}/hello_world.bin)

