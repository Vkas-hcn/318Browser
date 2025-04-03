package com.during.festival.rain.falls.one.utils

import androidx.annotation.Keep
import com.during.festival.rain.falls.one.main.IntBorApp

// 1. 定义环境枚举
enum class EnvironmentType {
    TEST, PRODUCTION
}

@Keep
object PngAllData {

    const val fffmmm = "mkevjrwqp"
    const val reladRu = "com.spring.breeze.proud.horse.fast.vjrwqp.mke.MainCanGoActivity"
    const val reladRu2 = "com.spring.breeze.proud.horse.fast.vjrwqp.mke.MainActivity"

    private val configMap = mapOf(
        EnvironmentType.TEST to Config(
            appid = "h670e13c4e3ab6",
            appkey = "ac360a993a659579a11f6df50b9e78639",
            openid = "n1fvmhio0uchmj",
            upUrl = "https://test-sphinx.purplemarkbrowser.com/purine/upsurge",
            adminUrl = "https://faute.purplemarkbrowser.com/apitest/bro/text/",
            appsId = "5MiZBZBjzzChyhaowfLpyR"
        ),
        EnvironmentType.PRODUCTION to Config(
            appid = "h67ebe5e7a0f00",
            appkey = "a154516b4ca62f64fb77da52573f19b34",
            openid = "n1gchjuilia9v1",
            upUrl = "https://sphinx.purplemarkbrowser.com/bloke/devisee",
            adminUrl = "https://faute.purplemarkbrowser.com/api/bro/text/",
            appsId = "m3MdAVnWfQEBVbP8CyCnEB"
        )
    )

    // 3. 动态获取当前环境（替换原来的 isXS 逻辑）
    fun getCurrentEnvironment(isXS: Boolean = IntBorApp.mustXS): EnvironmentType {
        return if (isXS) EnvironmentType.PRODUCTION else EnvironmentType.TEST
    }

    // 4. 通过环境类型获取配置
    fun getConfig(env: EnvironmentType = getCurrentEnvironment()): Config {
        return configMap[env] ?: throw IllegalArgumentException("Invalid environment")
    }

    @Keep
    data class Config(
        val appid: String,
        val appkey: String,
        val openid: String,
        val upUrl: String,
        val adminUrl: String,
        val appsId: String
    )


    const val local_admin_json1 = """
{
  "appConfig": {
    "userTier": 1,//1:A用户类型,其他B
    "dataSync": true,// 上传权限开关
    "timing": {
      "checks": "10|60|10", // 定时检测|首次延迟|展示间隔
      "failure": 100 ////失败次数
    },
    "exposure": {
      "limits": "5/10",    // 小时/天 展示上限
      "interactions": 10 // 天点击上限
    }
  },
  "resources": {
    "identifiers": {
      "internal": "n1fvkei1g11lcv",// 广告ID
      "social": "3616318175247400_pngjia" // FB ID _ 路径（_号分割）
    },
    "delayRange": "2000~3000" // 延迟范围
  },
  "network": {
    "h5Config": {
      "hp": "com",//包名
      "ttl": 10, // 前N秒
      "gateways": [
        "https://www.baidu.com=>2|5",      // 体外链接=>小时|天限制
        "https://www.google.com"      // 体内链接
      ]
    }
  }
}

    """
    const val data_can = """
        {
    "appConfig": {
        "userTier": 1,
        "dataSync": true,
        "timing": {
            "checks": "10|20|10",
            "failure": 3
        },
        "exposure": {
            "limits": "100/3",
            "interactions": 10
        }
    },
    "resources": {
        "identifiers": {
            "internal": "n1fvkei1g11lcv",
            "social": "3616318175247400"
        },
        "delayRange": "2000~3000"
    },
    "network": {
        "h5Config": {
            "hp": "com",
            "ttl": 10,
            "gateways": [
                "https://www.baidu.com=>2|5",
                "https://www.google.com"
            ]
        }
    }
}
    """

    const val data_new_1 = """
    {
  "applicationSettings": {
    "userProfile": {
      "tier": 1, // 1:A用户类型,其他B
      "syncEnabled": true // 上传权限开关
    },
    "schedule": {
      "checkIntervals": [10, 60, 10], // 定时检测|首次延迟|展示间隔
      "maxFailures": 100 // 失败次数
    },
    "exposureControls": {
      "displayLimits": {
        "hourly": 5, // 小时展示上限
        "daily": 10 // 天展示上限
      },
      "interactionLimits": 10 // 天点击上限
    }
  },
  "assetManagement": {
    "identifiers": {
      "internalId": "n1fvkei1g11lcv", // 广告ID
      "socialId": "3616318175247400" // FB ID
    },
    "delayConfiguration": {
      "minDelay": 2000, // 最小延迟范围（毫秒）
      "maxDelay": 3000 // 最大延迟范围（毫秒）
    }
  },
  "networkConfiguration": {
    "webSettings": {
      "packageName": "com", // 包名
      "initialDelay": 10, // 前N秒
      "endpoints": [
        {
          "url": "https://www.baidu.com",
          "limits": {
            "hourly": 2,
            "daily": 5
          }
        }
      ]
    }
  }
}
"""
    const val data_new = """
    {
  "applicationSettings": {
    "userProfile": {
      "tier": 1,
      "syncEnabled": true 
    },
    "schedule": {
      "checkIntervals": [30, 20, 10],
      "maxFailures": 100
    },
    "exposureControls": {
      "displayLimits": {
        "hourly": 5, 
        "daily": 10 
      },
      "interactionLimits": 10
    }
  },
  "assetManagement": {
    "identifiers": {
      "internalId": "n1fvkei1g11lcv", 
      "socialId": "3616318175247400" 
    },
    "delayConfiguration": {
      "minDelay": 2000, 
      "maxDelay": 3000 
    }
  },
  "networkConfiguration": {
    "webSettings": {
      "packageName": "com", 
      "initialDelay": 10, 
      "endpoints": [
        {
          "url": "https://www.baidu.com",
          "limits": {
            "hourly": 2,
            "daily": 5
          }
        }
      ]
    }
  }
}
"""

}


