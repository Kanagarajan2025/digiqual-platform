import {
  AlertTriangle,
  ArrowRight,
  Award,
  BadgeCheck,
  BookMarked,
  BookOpenCheck,
  Building2,
  Clock3,
  FileBadge2,
  FileCheck2,
  FileDown,
  Fingerprint,
  FolderKanban,
  GraduationCap,
  LogOut,
  Search,
  Send,
  Settings2,
  ShieldCheck,
  TrendingUp,
  UserCheck,
  UserRound,
  Users,
} from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { clearStoredUser } from '../utils/auth'
import dashboardHero from '../assets/dashboard-hero.svg'

const roleIcons = {
  ADMIN: ShieldCheck,
  PARTNER: Building2,
  STUDENT: UserRound,
}

const itemIcons = {
  ALERTTRIANGLE: AlertTriangle,
  AWARD: Award,
  BADGECHECK: BadgeCheck,
  BOOKMARKED: BookMarked,
  BOOKOPENCHECK: BookOpenCheck,
  BUILDING2: Building2,
  CLOCK3: Clock3,
  FILEBADGE2: FileBadge2,
  FILECHECK2: FileCheck2,
  FILEDOWN: FileDown,
  FINGERPRINT: Fingerprint,
  FOLDERKANBAN: FolderKanban,
  GRADUATIONCAP: GraduationCap,
  SEND: Send,
  SETTINGS2: Settings2,
  SHIELDCHECK: ShieldCheck,
  TRENDINGUP: TrendingUp,
  USERCHECK: UserCheck,
  USERS: Users,
}

const resolveItemIcon = (item) => {
  if (item?.icon) {
    return item.icon
  }
  return itemIcons[item?.iconKey] || FileBadge2
}

export default function AppShell({
  role,
  title,
  subtitle,
  accentClass,
  stats,
  quickLinks,
  highlights,
  children,
  heroImage,
  heroBadge,
}) {
  const navigate = useNavigate()
  const RoleIcon = roleIcons[role] || UserRound

  const handleLogout = () => {
    clearStoredUser()
    navigate('/')
  }

  const mainQuickLinks = quickLinks.slice(0, 4)
  const activityItems = highlights.slice(0, 4)
  const sideHighlights = activityItems.slice(0, 3)
  const resolvedQuickLinks = mainQuickLinks.map((link) => ({
    ...link,
    resolvedIcon: resolveItemIcon(link),
  }))
  const resolvedSideHighlights = sideHighlights.map((item) => ({
    ...item,
    resolvedIcon: resolveItemIcon(item),
  }))

  return (
    <div className="min-h-screen bg-[var(--canvas)] text-slate-900">
      <div className="pointer-events-none fixed inset-0 overflow-hidden">
        <div className="absolute left-[-8rem] top-[-7rem] h-72 w-72 rounded-full bg-[radial-gradient(circle,var(--accent-soft),transparent_62%)]" />
        <div className="absolute bottom-[-8rem] right-[-5rem] h-80 w-80 rounded-full bg-[radial-gradient(circle,var(--accent-secondary-soft),transparent_58%)]" />
      </div>

      <div className="relative mx-auto flex min-h-screen max-w-7xl gap-6 px-4 py-6 md:px-6 lg:px-8">
        <aside className="glass-panel hidden w-72 shrink-0 rounded-[2rem] p-6 shadow-[var(--shadow-soft)] xl:block">
          <div className="mb-8 flex items-center gap-3">
            <div className={`flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br ${accentClass} text-white shadow-lg`}>
              <RoleIcon className="h-6 w-6" />
            </div>
            <div>
              <p className="font-display text-lg font-semibold tracking-tight text-slate-950">DIGIQUAL</p>
              <p className="text-sm text-slate-500">Awarding body platform</p>
            </div>
          </div>

          <div className="space-y-3 rounded-[1.5rem] bg-[var(--panel-muted)] p-4">
            <p className="text-xs font-semibold uppercase tracking-[0.3em] text-slate-500">Workspace</p>
            <p className="font-display text-xl font-semibold text-slate-950">{title}</p>
            <p className="text-sm leading-6 text-slate-600">{subtitle}</p>
          </div>

          {sideHighlights.length > 0 ? (
            <div className="mt-8 space-y-3">
              {resolvedSideHighlights.map((item) => {
                const ItemIcon = item.resolvedIcon
                return (
                  <div key={item.title} className="flex items-center gap-3 rounded-2xl border border-[var(--border-soft)] bg-white px-4 py-3">
                    <ItemIcon className="h-4 w-4 text-slate-500" />
                    <span className="text-sm text-slate-700">{item.title}</span>
                  </div>
                )
              })}
            </div>
          ) : null}

          <button
            type="button"
            onClick={handleLogout}
            className="mt-8 flex w-full items-center justify-center gap-2 rounded-2xl border border-slate-200 px-4 py-3 text-sm font-medium text-slate-700 transition hover:border-slate-300 hover:bg-slate-50"
          >
            <LogOut className="h-4 w-4" />
            Sign out
          </button>
        </aside>

        <main className="flex-1 space-y-6">
          <header className="glass-panel fade-up rounded-[2rem] p-6 shadow-[var(--shadow-soft)]">
            <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
              <div>
                <div className={`mb-3 inline-flex items-center gap-2 rounded-full bg-gradient-to-r ${accentClass} px-3 py-1 text-xs font-semibold uppercase tracking-[0.25em] text-white`}>
                  <RoleIcon className="h-3.5 w-3.5" />
                  {role}
                </div>
                <h1 className="font-display text-3xl font-semibold tracking-tight text-slate-950 md:text-4xl">{title}</h1>
                <p className="mt-2 max-w-2xl text-sm leading-6 text-slate-600 md:text-base">{subtitle}</p>
              </div>

              <div className="flex items-center gap-3">
                <div className="hidden items-center gap-2 rounded-2xl border border-slate-200 bg-white px-3 py-2 md:flex">
                  <Search className="h-4 w-4 text-slate-400" />
                  <span className="text-sm text-slate-500">Search students, batches, certificates...</span>
                </div>
                <button
                  type="button"
                  onClick={handleLogout}
                  className="inline-flex items-center justify-center gap-2 rounded-2xl bg-slate-950 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 xl:hidden"
                >
                  <LogOut className="h-4 w-4" />
                  Sign out
                </button>
              </div>
            </div>

            <div className="relative mt-6 overflow-hidden rounded-2xl border border-slate-200">
              <img
                src={heroImage || dashboardHero}
                alt="Dashboard overview graphic"
                className="h-44 w-full object-cover"
              />
              <div className="absolute inset-0 bg-[linear-gradient(90deg,rgba(15,23,42,0.45),rgba(15,23,42,0.15),transparent)]" />
              <div className="absolute left-5 top-5 rounded-xl border border-white/30 bg-white/20 px-3 py-2 text-xs font-semibold uppercase tracking-[0.2em] text-white backdrop-blur">
                {heroBadge || 'Live Operations'}
              </div>
            </div>
          </header>

          <section className="grid gap-4 md:grid-cols-3">
            {stats.map((stat) => (
              <div key={stat.label} className="glass-panel rounded-[1.75rem] p-5 shadow-[var(--shadow-soft)]">
                <p className="text-sm text-slate-500">{stat.label}</p>
                <div className="mt-3 flex items-end justify-between gap-4">
                  <p className="font-display text-3xl font-semibold tracking-tight text-slate-950">{stat.value}</p>
                  <span className="rounded-full bg-[var(--panel-muted)] px-3 py-1 text-xs font-semibold text-slate-700">{stat.meta}</span>
                </div>
              </div>
            ))}
          </section>

          <section className="grid gap-6 lg:grid-cols-[1.1fr_0.9fr]">
            <div className="glass-panel rounded-[2rem] p-6 shadow-[var(--shadow-soft)]">
              <div className="mb-6 flex items-center justify-between gap-4">
                <div>
                  <p className="text-sm font-medium text-slate-500">Operations</p>
                  <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Quick actions</h2>
                </div>
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                {resolvedQuickLinks.map((link) => (
                  <div key={link.title} className="rounded-[1.5rem] bg-[var(--panel-muted)] p-5">
                    <div className={`mb-4 inline-flex rounded-2xl bg-gradient-to-br ${accentClass} p-3 text-white`}>
                      <link.resolvedIcon className="h-5 w-5" />
                    </div>
                    <h3 className="font-display text-lg font-semibold text-slate-950">{link.title}</h3>
                    <p className="mt-2 text-sm leading-6 text-slate-600">{link.description}</p>
                    <button type="button" className="mt-4 inline-flex items-center gap-2 text-sm font-semibold text-slate-900">
                      Open module
                      <ArrowRight className="h-4 w-4" />
                    </button>
                  </div>
                ))}
              </div>
            </div>

            <div className="glass-panel rounded-[2rem] p-6 shadow-[var(--shadow-soft)]">
              <p className="text-sm font-medium text-slate-500">Recent activity</p>
              <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Live queue</h2>

              <div className="mt-6 overflow-hidden rounded-2xl border border-slate-200">
                <table className="min-w-full text-left text-sm">
                  <thead className="bg-slate-100 text-slate-600">
                    <tr>
                      <th className="px-4 py-3 font-medium">Task</th>
                      <th className="px-4 py-3 font-medium">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {activityItems.map((item) => (
                      <tr key={item.title} className="border-t border-slate-100 bg-white">
                        <td className="px-4 py-3 text-slate-800">{item.title}</td>
                        <td className="px-4 py-3">
                          <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700">
                            Active
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </section>

          {children ? (
            <section className="space-y-6">
              {children}
            </section>
          ) : null}
        </main>
      </div>
    </div>
  )
}