require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.author       = package['author']
  s.platform     = :ios, "7.0"
  s.source       = { :git => package['repository']['url'], :tag => "master" }
  s.requires_arc    = true
  s.default_subspec = "Core"

  s.subspec "Core" do |ss|
    ss.source_files  = "mediation/ios/shared/**/*.{h,m}"
    ss.dependency "React"
  end

  s.subspec "AdMob" do |ss|
    ss.source_files  = "mediation/ios/admob/**/*.{h,m}"
    ss.dependency "#{package['name']}/Core"
    ss.dependency "Google-Mobile-Ads-SDK"
  end

  s.subspec "MoPub" do |ss|
    ss.source_files  = "mediation/ios/mopub/**/*.{h,m}"
    ss.dependency "#{package['name']}/Core"
    ss.dependency 'mopub-ios-sdk'
    ss.frameworks = 'AdSupport', 'AVFoundation', 'CoreGraphics', 'CoreLocation', 'Coremedia', 'CoreTelephony', 'Foundation', 'MediaPlayer', 'MessageUI', 'QuartzCore', 'SafariServices', 'StoreKit', 'SystemConfiguration', 'UIKit', 'WebKit'
  end

end



  