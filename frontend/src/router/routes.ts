export default [
  {
    path: '/login',
    name: 'login',
    component: async () => await import('src/views/LoginView.vue'),
  },
  {
    path: '/cadastro',
    name: 'cadastro',
    component: async () => await import('src/views/CadastroView.vue'),
  },
  {
    path: '/',
    name: 'protected',
    redirect: () => { return 'home' },
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '/',
        name: 'navigation',
        component: async () => await import('src/views/NavigationView.vue'),
        redirect: () => { return 'home' },
        children: [
          {
            path: '/home',
            name: 'home',
            component: async () => await import('src/views/HomeView.vue'),
          },
        ]
      },
    ]
  },
]