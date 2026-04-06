import { useState } from 'react'
import { Mail, Lock, Building2, GraduationCap, Shield, Eye, EyeOff, Loader } from 'lucide-react'
import { login } from '../api/authApi'

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [selectedRole, setSelectedRole] = useState('STUDENT')
  const [showPassword, setShowPassword] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const roles = [
    {
      id: 'STUDENT',
      label: 'Student',
      icon: GraduationCap,
      description: 'Access your courses & certificates',
      color: 'from-blue-500 to-cyan-500',
    },
    {
      id: 'PARTNER',
      label: 'Partner Institute',
      icon: Building2,
      description: 'Manage batches & students',
      color: 'from-purple-500 to-pink-500',
    },
    {
      id: 'ADMIN',
      label: 'Super Admin',
      icon: Shield,
      description: 'Full platform control',
      color: 'from-amber-500 to-orange-500',
    },
  ]

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
      const response = await login(email, password, selectedRole)
      
      if (response.success) {
        setSuccess('Login successful! Redirecting...')
        setTimeout(() => {
          window.location.href = `/${selectedRole.toLowerCase()}-dashboard`
        }, 1500)
      } else {
        setError(response.message || 'Login failed. Please check your credentials.')
      }
    } catch (err) {
      setError('An error occurred. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const getSelectedRole = () => roles.find(r => r.id === selectedRole)
  const SelectedIcon = getSelectedRole()?.icon

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-8">
      <div className="w-full max-w-6xl grid md:grid-cols-2 gap-8 items-center">
        {/* Left Side - Branding */}
        <div className="hidden md:flex flex-col justify-center items-center text-white text-center md:text-left space-y-8">
          <div className="space-y-4">
            <div className="text-6xl font-bold bg-gradient-to-r from-cyan-400 to-blue-400 bg-clip-text text-transparent">
              DIGIQUAL
            </div>
            <p className="text-xl text-slate-300 font-light">
              Awarding Body Management Platform
            </p>
            <p className="text-slate-400 leading-relaxed">
              Centralized system for managing partner institutes, student records, and certificate issuance.
            </p>
          </div>

          {/* Features */}
          <div className="space-y-6">
            <div className="flex gap-4 items-start">
              <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-cyan-500 to-blue-500 flex items-center justify-center flex-shrink-0">
                <GraduationCap className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white mb-1">For Students</h3>
                <p className="text-sm text-slate-400">Access courses, track progress & download certificates</p>
              </div>
            </div>

            <div className="flex gap-4 items-start">
              <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center flex-shrink-0">
                <Building2 className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white mb-1">For Partners</h3>
                <p className="text-sm text-slate-400">Create batches, enrol students & manage submissions</p>
              </div>
            </div>

            <div className="flex gap-4 items-start">
              <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-amber-500 to-orange-500 flex items-center justify-center flex-shrink-0">
                <Shield className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white mb-1">For Admins</h3>
                <p className="text-sm text-slate-400">Approve partners, manage certificates & oversee system</p>
              </div>
            </div>
          </div>
        </div>

        {/* Right Side - Login Form */}
        <div className="flex items-center justify-center">
          <div className="w-full max-w-md">
            {/* Card Background */}
            <div className="relative">
              {/* Glow effect */}
              <div className="absolute inset-0 bg-gradient-to-br from-cyan-500/20 to-blue-500/20 rounded-2xl blur-xl"></div>

              {/* Card */}
              <div className="relative bg-white/10 backdrop-blur-xl rounded-2xl p-8 border border-white/20 shadow-2xl">
                {/* Logo for mobile */}
                <div className="md:hidden mb-8 text-center">
                  <div className="text-4xl font-bold bg-gradient-to-r from-cyan-400 to-blue-400 bg-clip-text text-transparent">
                    DIGIQUAL
                  </div>
                  <p className="text-slate-400 text-sm mt-2">Login to your account</p>
                </div>

                {/* Role Selector */}
                <div className="mb-8 space-y-4">
                  <label className="text-white text-sm font-semibold block mb-4">Select your role</label>
                  <div className="grid grid-cols-3 gap-3">
                    {roles.map((role) => {
                      const Icon = role.icon
                      const isSelected = selectedRole === role.id

                      return (
                        <button
                          key={role.id}
                          onClick={() => setSelectedRole(role.id)}
                          className={`p-4 rounded-lg border-2 transition-all duration-300 flex flex-col items-center justify-center gap-2 h-full ${
                            isSelected
                              ? `border-white/60 bg-gradient-to-br ${role.color} shadow-lg`
                              : 'border-white/10 bg-white/5 hover:bg-white/10 text-slate-300'
                          }`}
                        >
                          <Icon className={`w-6 h-6 ${isSelected ? 'text-white' : ''}`} />
                          <span className={`text-xs font-semibold ${isSelected ? 'text-white' : ''}`}>
                            {role.label}
                          </span>
                        </button>
                      )
                    })}
                  </div>

                  {/* Role Description */}
                  <div className="mt-4 p-3 rounded-lg bg-white/5 border border-white/10">
                    <p className="text-sm text-slate-300">
                      {getSelectedRole()?.description}
                    </p>
                  </div>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="space-y-5">
                  {/* Email Input */}
                  <div>
                    <label className="text-white text-sm font-semibold block mb-2">
                      Email Address
                    </label>
                    <div className="relative">
                      <Mail className="absolute left-4 top-3.5 w-5 h-5 text-slate-400" />
                      <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="you@example.com"
                        className="w-full bg-white/10 border border-white/20 rounded-lg pl-12 pr-4 py-3 text-white placeholder-slate-400 focus:outline-none focus:border-cyan-500 focus:bg-white/15 transition-all"
                      />
                    </div>
                  </div>

                  {/* Password Input */}
                  <div>
                    <label className="text-white text-sm font-semibold block mb-2">
                      Password
                    </label>
                    <div className="relative">
                      <Lock className="absolute left-4 top-3.5 w-5 h-5 text-slate-400" />
                      <input
                        type={showPassword ? 'text' : 'password'}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        className="w-full bg-white/10 border border-white/20 rounded-lg pl-12 pr-12 py-3 text-white placeholder-slate-400 focus:outline-none focus:border-cyan-500 focus:bg-white/15 transition-all"
                      />
                      <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        className="absolute right-4 top-3.5 text-slate-400 hover:text-white transition-colors"
                      >
                        {showPassword ? (
                          <EyeOff className="w-5 h-5" />
                        ) : (
                          <Eye className="w-5 h-5" />
                        )}
                      </button>
                    </div>
                  </div>

                  {/* Error Message */}
                  {error && (
                    <div className="p-4 rounded-lg bg-red-500/20 border border-red-500/50 text-red-200 text-sm">
                      {error}
                    </div>
                  )}

                  {/* Success Message */}
                  {success && (
                    <div className="p-4 rounded-lg bg-green-500/20 border border-green-500/50 text-green-200 text-sm">
                      {success}
                    </div>
                  )}

                  {/* Login Button */}
                  <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-600 hover:to-blue-600 disabled:from-slate-600 disabled:to-slate-700 text-white font-semibold py-3 rounded-lg transition-all duration-300 flex items-center justify-center gap-2 shadow-lg hover:shadow-xl disabled:shadow-none"
                  >
                    {loading ? (
                      <>
                        <Loader className="w-5 h-5 animate-spin" />
                        Authenticating...
                      </>
                    ) : (
                      <>
                        <Lock className="w-5 h-5" />
                        Login to DIGIQUAL
                      </>
                    )}
                  </button>
                </form>

                {/* Footer */}
                <div className="mt-8 pt-6 border-t border-white/10 text-center space-y-4">
                  <div className="flex items-center justify-center gap-2 text-slate-400 text-sm">
                    <span>Demo Credentials</span>
                  </div>
                  <div className="grid grid-cols-3 gap-2 text-xs">
                    <div className="bg-white/5 rounded p-2 border border-white/10">
                      <p className="font-semibold text-slate-300 mb-1">Student</p>
                      <p className="text-slate-500 break-all">student@test.com</p>
                    </div>
                    <div className="bg-white/5 rounded p-2 border border-white/10">
                      <p className="font-semibold text-slate-300 mb-1">Partner</p>
                      <p className="text-slate-500 break-all">partner@test.com</p>
                    </div>
                    <div className="bg-white/5 rounded p-2 border border-white/10">
                      <p className="font-semibold text-slate-300 mb-1">Admin</p>
                      <p className="text-slate-500 break-all">admin@test.com</p>
                    </div>
                  </div>
                  <p className="text-slate-500 text-xs">Pass: password123</p>
                </div>
              </div>
            </div>

            {/* Footer text */}
            <p className="text-center text-slate-400 text-sm mt-6">
              © 2024 DIGIQUAL Ltd. All rights reserved.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
