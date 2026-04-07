import { useState } from 'react'
import {
  ArrowRight,
  Building2,
  CheckCircle2,
  Eye,
  EyeOff,
  GraduationCap,
  KeyRound,
  Landmark,
  Loader,
  Lock,
  Mail,
  Shield,
} from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { Link } from 'react-router-dom'
import { login } from '../api/authApi'
import loginIllustration from '../assets/login-illustration.svg'

export default function LoginPage({ defaultRole = 'STUDENT' }) {
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [selectedRole, setSelectedRole] = useState(defaultRole)
  const [showPassword, setShowPassword] = useState(false)
  const [rememberMe, setRememberMe] = useState(true)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [visualFallback, setVisualFallback] = useState(false)

  const roles = [
    {
      id: 'STUDENT',
      label: 'Student',
      icon: GraduationCap,
      description: 'Learning portal and certificate access',
      color: 'from-orange-400 to-orange-600',
    },
    {
      id: 'PARTNER',
      label: 'Partner',
      icon: Building2,
      description: 'Batch, enrolment, and submission workspace',
      color: 'from-amber-400 to-orange-500',
    },
    {
      id: 'ADMIN',
      label: 'Admin',
      icon: Shield,
      description: 'Approvals, compliance, and certificate controls',
      color: 'from-orange-500 to-red-500',
    },
  ]

  const demoCredentials = {
    STUDENT: { email: 'student@digiqual.com', password: 'Student@123' },
    PARTNER: { email: 'partner@digiqual.com', password: 'Partner@123' },
    ADMIN: { email: 'admin@digiqual.com', password: 'Admin@123' },
  }

  const selectedRoleDetails = roles.find((role) => role.id === selectedRole)
  const SelectedRoleIcon = selectedRoleDetails.icon

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    setLoading(true)

    if (!email || !password) {
      setError('Please fill in all fields')
      setLoading(false)
      return
    }

    try {
      const response = await login(email.trim(), password, selectedRole.trim())
      if (response.success) {
        setSuccess('Login successful. Redirecting...')
        setTimeout(() => {
          navigate(`/${selectedRole.toLowerCase()}-dashboard`)
        }, 1200)
      } else {
        setError(response.message || 'Login failed. Please verify your credentials.')
      }
    } catch (err) {
      setError('An error occurred. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const fillDemoCredentials = (roleId) => {
    const credentials = demoCredentials[roleId]
    setSelectedRole(roleId)
    setEmail(credentials.email)
    setPassword(credentials.password)
    setError('')
    setSuccess('')
  }

  return (
    <div className="relative min-h-screen overflow-hidden px-4 py-8 md:px-6 lg:px-8">
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute -left-20 -top-24 h-80 w-80 rounded-full bg-orange-200/35 blur-3xl" />
        <div className="absolute -right-20 bottom-0 h-80 w-80 rounded-full bg-amber-200/30 blur-3xl" />
      </div>

      <div className="relative mx-auto max-w-6xl rounded-[2rem] border border-slate-200 bg-white shadow-[0_24px_90px_rgba(15,23,42,0.12)]">
        <div className="grid min-h-[680px] lg:grid-cols-[0.95fr_1.05fr]">
          <section className="relative overflow-hidden rounded-t-[2rem] bg-gradient-to-br from-orange-500 via-orange-600 to-orange-700 p-6 text-white lg:rounded-l-[2rem] lg:rounded-tr-none lg:p-10">
            <div className="absolute inset-0 opacity-25 [background:radial-gradient(circle_at_20%_10%,#fff_1px,transparent_1px)] [background-size:22px_22px]" />
            <div className="relative z-10">
              <div className="inline-flex items-center gap-2 rounded-full bg-white/15 px-3 py-1 text-xs font-semibold uppercase tracking-[0.18em]">
                <Landmark className="h-3.5 w-3.5" />
                DIGIQUAL
              </div>

              <h1 className="mt-5 font-display text-4xl font-semibold leading-tight md:text-5xl">
                Trusted certification,
                <br />
                one secure login.
              </h1>

              <p className="mt-4 max-w-md text-sm leading-7 text-orange-50/95">
                Designed for UK awarding-body workflows: partner onboarding, student verification, and certificate governance.
              </p>

              {visualFallback ? (
                <div className="mt-8 rounded-3xl border border-white/25 bg-white/12 p-6 backdrop-blur">
                  <p className="text-xs font-semibold uppercase tracking-[0.18em] text-orange-50">Platform Capabilities</p>
                  <div className="mt-4 space-y-3 text-sm">
                    <div className="flex items-center gap-2"><CheckCircle2 className="h-4 w-4" />Role-based access control</div>
                    <div className="flex items-center gap-2"><CheckCircle2 className="h-4 w-4" />Partner and student approvals</div>
                    <div className="flex items-center gap-2"><CheckCircle2 className="h-4 w-4" />Certificate issue and verification</div>
                  </div>
                </div>
              ) : (
                <div className="mt-8 overflow-hidden rounded-3xl border border-white/25 bg-white/10">
                  <img
                    src={loginIllustration}
                    alt="DIGIQUAL login workspace preview"
                    onError={() => setVisualFallback(true)}
                    className="h-[320px] w-full object-cover"
                  />
                </div>
              )}

              <div className="mt-6 grid grid-cols-3 gap-3">
                <div className="rounded-xl bg-white/12 px-3 py-2 text-center">
                  <p className="text-[11px] uppercase tracking-[0.15em] text-orange-50/80">Portals</p>
                  <p className="mt-1 text-lg font-semibold">3</p>
                </div>
                <div className="rounded-xl bg-white/12 px-3 py-2 text-center">
                  <p className="text-[11px] uppercase tracking-[0.15em] text-orange-50/80">Auth</p>
                  <p className="mt-1 text-lg font-semibold">JWT</p>
                </div>
                <div className="rounded-xl bg-white/12 px-3 py-2 text-center">
                  <p className="text-[11px] uppercase tracking-[0.15em] text-orange-50/80">State</p>
                  <p className="mt-1 text-lg font-semibold">Live</p>
                </div>
              </div>
            </div>
          </section>

          <section className="flex flex-col justify-center p-6 md:p-10">
            <div className="mx-auto w-full max-w-lg">
              <div className="mb-6">
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Login</p>
                <h2 className="mt-2 font-display text-3xl font-semibold text-slate-950">Welcome to DIGIQUAL</h2>
                <p className="mt-2 text-sm text-slate-600">Use the approved email and password for your portal role.</p>
              </div>

              <div className="mb-5 grid grid-cols-3 gap-2">
                {roles.map((role) => {
                  const Icon = role.icon
                  const selected = selectedRole === role.id
                  return (
                    <button
                      key={role.id}
                      type="button"
                      onClick={() => setSelectedRole(role.id)}
                      className={`rounded-xl border px-3 py-2 text-left transition ${
                        selected
                          ? 'border-orange-500 bg-orange-50 text-orange-700'
                          : 'border-slate-200 bg-white text-slate-700 hover:border-slate-300'
                      }`}
                    >
                      <Icon className="h-4 w-4" />
                      <p className="mt-1 text-xs font-semibold uppercase tracking-[0.12em]">{role.label}</p>
                    </button>
                  )
                })}
              </div>

              <div className="mb-5 rounded-xl border border-orange-100 bg-orange-50 px-4 py-3">
                <div className="flex items-center gap-2">
                  <SelectedRoleIcon className="h-4 w-4 text-orange-600" />
                  <p className="text-sm font-semibold text-orange-800">{selectedRoleDetails.label} Access</p>
                </div>
                <p className="mt-1 text-xs text-orange-700">{selectedRoleDetails.description}</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <label className="mb-1.5 block text-sm font-medium text-slate-700">Email</label>
                  <div className="relative">
                    <Mail className="pointer-events-none absolute left-3.5 top-3.5 h-4 w-4 text-slate-400" />
                    <input
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      placeholder="approved-user@digiqual.com"
                      className="w-full rounded-xl border border-slate-300 bg-white py-3 pl-10 pr-4 text-sm text-slate-900 outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-100"
                    />
                  </div>
                </div>

                <div>
                  <label className="mb-1.5 block text-sm font-medium text-slate-700">Password</label>
                  <div className="relative">
                    <Lock className="pointer-events-none absolute left-3.5 top-3.5 h-4 w-4 text-slate-400" />
                    <input
                      type={showPassword ? 'text' : 'password'}
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="Enter your password"
                      className="w-full rounded-xl border border-slate-300 bg-white py-3 pl-10 pr-11 text-sm text-slate-900 outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-100"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-3 text-slate-500 hover:text-slate-800"
                    >
                      {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                    </button>
                  </div>
                </div>

                <div className="flex items-center justify-between text-xs">
                  <label className="inline-flex cursor-pointer items-center gap-2 text-slate-600">
                    <input
                      type="checkbox"
                      checked={rememberMe}
                      onChange={(e) => setRememberMe(e.target.checked)}
                      className="h-4 w-4 rounded border-slate-300 text-orange-600 focus:ring-orange-400"
                    />
                    Remember me
                  </label>
                  <button type="button" className="font-medium text-orange-600 hover:text-orange-700">Forgot password?</button>
                </div>

                {error && <div className="rounded-xl border border-rose-200 bg-rose-50 px-4 py-2 text-sm text-rose-700">{error}</div>}
                {success && <div className="rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-2 text-sm text-emerald-700">{success}</div>}

                <button
                  type="submit"
                  disabled={loading}
                  className="inline-flex w-full items-center justify-center gap-2 rounded-xl bg-gradient-to-r from-orange-500 to-orange-600 px-4 py-3 text-sm font-semibold text-white shadow-lg shadow-orange-200 transition hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  {loading ? (
                    <>
                      <Loader className="h-4 w-4 animate-spin" />
                      Authenticating
                    </>
                  ) : (
                    <>
                      Sign in
                      <ArrowRight className="h-4 w-4" />
                    </>
                  )}
                </button>
              </form>

              <div className="mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4">
                <p className="mb-2 text-xs font-semibold uppercase tracking-[0.14em] text-slate-500">Demo Accounts</p>
                <div className="grid gap-2 sm:grid-cols-3">
                  {roles.map((role) => (
                    <button
                      key={`${role.id}-demo`}
                      type="button"
                      onClick={() => fillDemoCredentials(role.id)}
                      className="rounded-lg border border-slate-200 bg-white px-2 py-2 text-left hover:border-orange-300"
                    >
                      <p className="text-xs font-semibold text-slate-800">{role.label}</p>
                      <p className="mt-1 truncate text-[11px] text-slate-500">{demoCredentials[role.id].email}</p>
                    </button>
                  ))}
                </div>
              </div>

              <div className="mt-4 flex items-center gap-2 text-xs text-slate-500">
                <KeyRound className="h-3.5 w-3.5" />
                Session is secured and role-validated before dashboard access.
              </div>

              <div className="mt-3 text-xs text-slate-600">
                Need to validate a certificate?{' '}
                <Link to="/certificate-verification" className="font-semibold text-orange-600 hover:text-orange-700">
                  Open public verification
                </Link>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  )
}
