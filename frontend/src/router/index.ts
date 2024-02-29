import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import LocalStorage from 'src/services/localStorage'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    // always scroll to top
    return { top: 0 }
  },
  routes
})

function usuarioAutenticado() {
  const token = storage.getToken()
  return token !== undefined && token !== ''
}

const storage = new LocalStorage();

router.beforeEach((to) => {
  if(to.name === 'login' && storage.getLoggedUser()) {
    return { name: 'protected' }
  }

  if(to.meta.requiresAuth && !storage.getLoggedUser()) {
    return { name: 'login' }
  }
})
/*
router.beforeEach((to, from) => {
  const isAutenticado = usuarioAutenticado();
  const loggedUser = storage.getLoggedUser();

  // Restrict protected routes if the user is not authenticated
  if (to.meta.requiresAuth === true && isAutenticado === false) {
    return { path: '/' };
  }

  // Force users with the role 'USER' to complete registration
  if (to.path !== '/cadastro' && loggedUser !== null && (to.meta?.shouldNotForce !== true) && loggedUser.role === 'User') {
    return { path: '/cadastro' };
  } else if (!isAutenticado) {
    // Prevent redirection to '/cadastro' if the user is already authenticated
    if (to.path === '/cadastro' && loggedUser !== null && loggedUser.role === 'User') {
      return { path: from.path };
    }

    // Check role permissions
    try {
      if (to.meta.requiresAuthAdmin) {
        const allowedScopes = to.meta.allowedScopes;

        // Handle potential undefined or object-type allowedScopes
        if (!allowedScopes || typeof allowedScopes !== 'object') {
          // Log or handle the unexpected type for debugging and security
          console.error("Unexpected 'allowedScopes' type:", allowedScopes);
          return { path: '/' }; // Or redirect to an error page
        }

        // Check if loggedUser role exists (optional)
        if (loggedUser?.role) {
          const hasPermission = Object.values(allowedScopes).includes(loggedUser.role); // Use Object.values for objects
          if (!hasPermission) {
            return { path: '/' };
          }
        } else {
          // Handle missing loggedUser role (optional)
          console.error("Missing loggedUser.role for permission check.");
          return { path: '/' }; // Or redirect to an appropriate location
        }
      }
    } catch (error) {
      console.error("Error verifying permissions:", error);
      return { path: '/' }; // Or redirect to a designated error page or inform the user
    }
  }
});*/

export default router