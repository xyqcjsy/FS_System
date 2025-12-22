<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="240px" class="layout-aside">
      <div class="logo">
        <el-icon size="32"><Money /></el-icon>
        <span class="logo-text">FS_System</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :unique-opened="true"
        router
        class="layout-menu"
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <!-- 首页特殊处理：显示为单个菜单项 -->
          <template v-if="route.path === '/'">
            <el-menu-item
              v-for="child in route.children"
              :key="child.path"
              :index="'/' + child.path"
            >
              <el-icon><component :is="child.meta.icon" /></el-icon>
              <span>{{ child.meta.title }}</span>
            </el-menu-item>
          </template>
          
          <!-- 有子菜单的其他路由 -->
          <el-sub-menu v-else-if="route.children && route.children.length > 0" :index="route.path">
            <template #title>
              <el-icon><component :is="route.meta.icon" /></el-icon>
              <span>{{ route.meta.title }}</span>
            </template>
            <el-menu-item
              v-for="child in route.children"
              :key="child.path"
              :index="route.path + '/' + child.path"
            >
              <el-icon><component :is="child.meta.icon" /></el-icon>
              <span>{{ child.meta.title }}</span>
            </el-menu-item>
          </el-sub-menu>
          
          <!-- 无子菜单 -->
          <el-menu-item v-else :index="route.path">
            <el-icon><component :is="route.meta.icon" /></el-icon>
            <span>{{ route.meta.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="layout-header">
        <div class="header-left">
          <span class="page-title">{{ currentPageTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon class="user-icon"><User /></el-icon>
              <span class="user-name">管理员</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
            
                <el-dropdown-item divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>

      <!-- 页脚 -->
      <el-footer class="layout-footer">
        <span>FS_System 财务会计管理系统 ©CSU商学院信管2302谢扬强 2025</span>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前路由
const currentRoute = computed(() => route)

// 当前页面标题
const currentPageTitle = computed(() => {
  if (route.path === '/' || route.path === '/dashboard') {
    return '工作台'
  }
  return route.meta?.title || '未命名页面'
})

// 菜单路由（包含首页，并调整顺序）
const menuRoutes = computed(() => {
  const allRoutes = router.options.routes
  const dashboardRoute = allRoutes.find(r => r.path === '/')
  const otherRoutes = allRoutes.filter(r => r.path !== '/' && r.children)
  
  // 首页放在最前面
  return dashboardRoute ? [dashboardRoute, ...otherRoutes] : otherRoutes
})
</script>

<style scoped lang="scss">
.layout-container {
  width: 100%;
  height: 100vh;
  background-color: var(--bg-body);
}

.layout-aside {
  background: #FFFFFF;
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 1000;
  width: 240px;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 1px;
    height: 100%;
    background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.06) 50%, transparent 100%);
  }
  
  .logo {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--text-primary);
    font-size: 22px;
    font-weight: 700;
    background: linear-gradient(135deg, #F7F9FC 0%, #FFFFFF 100%);
    border-bottom: 1px solid var(--border-color);
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: radial-gradient(circle at 20% 50%, rgba(38, 132, 255, 0.03), transparent 50%);
    }
    
    .el-icon {
      color: #2684FF;
      filter: drop-shadow(0 0 4px rgba(38, 132, 255, 0.3));
      animation: pulse 2s ease-in-out infinite;
    }
    
    .logo-text {
      margin-left: 12px;
      letter-spacing: 1px;
      background: linear-gradient(135deg, var(--text-primary) 0%, #2684FF 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }
  
  .layout-menu {
    border-right: none;
    background: transparent;
    padding: 8px 0;
    
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      color: var(--text-primary);
      margin: 4px 12px;
      border-radius: 8px;
      transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
      height: 48px;
      line-height: 48px;
      
      &:hover {
        background: linear-gradient(135deg, rgba(38, 132, 255, 0.08) 0%, rgba(38, 132, 255, 0.12) 100%) !important;
        color: var(--primary-color);
        transform: translateX(4px);
        box-shadow: 0 2px 8px rgba(38, 132, 255, 0.15);
      }
      
      .el-icon {
        font-size: 18px;
        margin-right: 8px;
      }
    }
    
    :deep(.el-menu-item.is-active) {
      background: linear-gradient(135deg, #2684FF 0%, #0052CC 100%) !important;
      color: #FFFFFF;
      box-shadow: 0 4px 16px rgba(38, 132, 255, 0.3);
      position: relative;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 24px;
        background: #FFFFFF;
        border-radius: 0 2px 2px 0;
      }
    }
    
    :deep(.el-sub-menu) {
      .el-menu {
        background-color: #F7F9FC !important;
        padding: 4px 0;
      }
      
      .el-menu-item {
        background-color: transparent !important;
        margin: 2px 8px 2px 16px;
        height: 42px;
        line-height: 42px;
        padding-left: 48px !important;
        
        &:hover {
          background: rgba(38, 132, 255, 0.08) !important;
          color: var(--primary-color);
        }
        
        &.is-active {
          background: linear-gradient(135deg, rgba(38, 132, 255, 0.15) 0%, rgba(38, 132, 255, 0.2) 100%) !important;
          color: var(--primary-color);
          font-weight: 600;
          
          &::before {
            width: 3px;
            height: 18px;
            background: var(--primary-color);
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    filter: drop-shadow(0 0 4px rgba(38, 132, 255, 0.3));
  }
  50% {
    filter: drop-shadow(0 0 8px rgba(38, 132, 255, 0.5));
  }
}

.layout-header {
  background: #FFFFFF;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  position: relative;
  z-index: 999;
  
  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg, 
      transparent 0%, 
      rgba(0, 82, 204, 0.1) 20%, 
      rgba(0, 82, 204, 0.1) 80%, 
      transparent 100%
    );
  }
  
  .header-left {
    display: flex;
    align-items: center;
    flex: 1;
    
    .page-title {
      font-size: 20px;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: 0.5px;
    }
  }
  
  .header-right {
    display: flex;
    align-items: center;
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      padding: 8px 16px;
      border-radius: var(--radius-xl);
      transition: all 0.25s;
      
      &:hover {
        background: linear-gradient(135deg, rgba(0, 82, 204, 0.06) 0%, rgba(0, 82, 204, 0.1) 100%);
        
        .user-icon,
        .user-name {
          color: var(--primary-color);
        }
      }
      
      .user-icon {
        font-size: 22px;
        color: var(--text-secondary);
        transition: color 0.25s;
      }
      
      .user-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
        transition: color 0.25s;
      }
    }
  }
  
  :deep(.el-dropdown-menu__item) {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 16px;
    
    .el-icon {
      font-size: 16px;
    }
  }
}

.layout-main {
  background-color: var(--bg-body);
  padding: 24px;
  overflow-y: auto;
  position: relative;
  
  > * {
    position: relative;
    z-index: 1;
  }
}

.layout-footer {
  background: linear-gradient(135deg, #FFFFFF 0%, #F7F9FC 100%);
  border-top: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  color: var(--text-secondary);
  height: 50px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.02);
  
  span {
    font-weight: 500;
    letter-spacing: 0.5px;
  }
}

// 过渡动画 - 更流畅
.fade-enter-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-leave-active {
  transition: all 0.2s cubic-bezier(0.4, 0, 1, 1);
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>

